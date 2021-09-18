package com.revature.models;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.dao.AccountDao;
import com.revature.dao.CustomerDao;

public class Menu {
	
	AccountDao aDao = new AccountDao();
	CustomerDao cDao = new CustomerDao();
	Logger log = LogManager.getLogger(Menu.class);
	Scanner scan = new Scanner(System.in);
	
	public void runMenu() {
		
		boolean done = false;
		//displayMenuText();
		while(!done) {
			displayMenuText(); //moved to separate function for aesthetics
			done = menuLogic(scan.nextLine());  //likewise
		}//end while
		scan.close();
	} //end runMenu()
	
	/**
	 * Contains all of the logic for the menu and terminates loop
	 * upon receiving exit command
	 * 
	 * @param selection String containing user selection
	 * @return boolean used to terminate while loop for runMenu()
	 */
	private boolean menuLogic(String selection){ //brace yourself, this is ugly
		switch(selection.toLowerCase()) { //TODO: Finish switch statement logic in each case including logging
		case "viewaccounts":{ //COMPLETED 09/18 2:21 PM
			ArrayList<Account> accounts;
			try {
				accounts = aDao.getAccounts();
				viewAccounts(accounts);
			} catch (SQLException e) {
				System.out.println("An error occured while accessing database");
				log.error(e.getMessage());
				e.printStackTrace();
			}//end try/catch
			break;
		}//end case
		case "viewcustomers":{//COMPLETED 09/17
			viewCustomers(); 
			break;
		}//end case
		case "viewaccountinfo":{
			System.out.println("Select customer: ");
			System.out.println();
			viewCustomers();
			int choice = parseUserInput();
			if (choice != -1){
				
			}
			else {
				System.out.println("The selected customer was not found.");
				log.warn("Invalid customer selection: " + choice);
			}
			//TODO: Implement Account Info viewing for customer selection
			break;
		}//end case
		case "newcustomer":{ 
			System.out.println("Enter new customer's first name: ");
			String f_name = scan.nextLine().strip();
			System.out.println("Enter new customer's first name: ");
			String l_name = scan.nextLine().strip();
			//System.out.println("");
			try {
				cDao.createCustomer(new Customer(f_name, l_name));
			} catch (SQLException e) {
				e.printStackTrace();
				log.error(e.getMessage());
			}//end try/catch
			log.info("User created new customer: " + f_name + " " + l_name); 
			ArrayList<Customer> result = null;
			try {
				result = cDao.getCustomersByName(f_name, l_name);
			} catch (SQLException e) {
				log.error(e.getMessage() + e.getSQLState());
				e.printStackTrace();
			}
			if (result != null) {
				//fetch last item's id, save for use in creating account
				int customer_id_fk = result.get(result.size() - 1).getCustomer_id(); //Java needs to allow [] notation for Lists
				System.out.println("Enter initial balance: ");
				double balance = parseDecimalInput();
				System.out.println("Enter type of account: ");
				String account_type = scan.nextLine().strip();
				aDao.addAccount(new Account(customer_id_fk, account_type, balance));
				break;
			}
		}//end case
		case "openaccount":{
			//TODO: Add open Account functionality
			break;
		}//end case
		case "closeaccounts":{
			System.out.println("Enter customer id to close accounts");
			viewCustomers();
			int choice = parseUserInput();
			if (choice != -1) {
				try {
					cDao.closeAccount(choice);
				} catch (SQLException e) {
					log.error(e.getMessage());
					e.printStackTrace();
				}
				log.warn("Customer " + choice + " accounts closed");
			}
			else {
				System.out.println("Invalid selection");
				log.warn("Invalid customer selection: " + choice);
			}
			
			
			break;
		}//end case
		case "internaltransfer":{
			//TODO: implement after completing deposit and withdrawal
			break;
		}//end case
		case "withdrawal":{
			//TODO: implement withdrawal function
			System.out.println("Which ");
			break;
		}
		case "deposit":{
			//TODO: implement withdrawal function
			break;
		}//end case
		case "exit":{
			break;
		}//end case
		case "sandbox":{
			System.out.println("Testing balance alteration");
			try {
				aDao.alterBalance(19, -10.0);
				aDao.alterBalance(20, 10.0);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println(e.getSQLState());
			}
			break;
		}
		default:{
			System.out.println("Command \"" + selection + "\" not found");
			System.out.println();
		}//end default
		}//end switch
		
		return selection.toLowerCase().equals("exit"); //strip spaces, clean input in separate method?
	}//end menuLogic

	/**
	 * @return double The value parsed from the user input
	 */
	private double parseDecimalInput() {
		boolean valid = false;
		double value = 0.0;
		while(!valid) {
			try {
				value = Double.parseDouble(scan.nextLine());
				valid = true;
			}
			catch (NumberFormatException e) {
				System.out.println("Please enter a numeric value.");
			}
		}
		return value;
	}

	/**
	 * Displays list of accounts
	 * @param accounts The list of accounts to be printed
	 */
	private void viewAccounts(ArrayList<Account> accounts) {
		for (Account a : accounts) {
			System.out.println(a);
		}
		
		System.out.println();
	}

	/**
	 * Parses out customer ID
	 * @return int customer id number, or -1 if error occurs
	 */
	private int parseUserInput() {
		int choice;
		try {
				choice = scan.nextInt();
				if (choice < 0) {
					return -1; //error code
				}
		}
		catch(InputMismatchException e){
			System.out.println("Please enter a numeric value");
			log.error("User entered non-numeric user ID");
			return -1; //error code
		}
		finally {
			scan.nextLine();//flush scanner input
		}
		return choice;
	}

	/**
	 * Displays list of all customers
	 */
	private void viewCustomers() {
		ArrayList<Customer> customers = null;
		try {
			customers = cDao.getCustomers();
		}
		catch(SQLException e) {
			log.error(e.getMessage());
		}
		if (customers != null) {
			viewCustomers(customers);
		}
	}

	/**
	 * Displays the list of customers passed
	 * @param customers The list of customers to be viewed
	 */
	private void viewCustomers(List<Customer> customers) {
		for (Customer c : customers) {
			System.out.println(c);
		}
		
		System.out.println();
	}
	
	
	/**
	 * prints the menu text, moved to separate function
	 * for aesthetic purposes
	 */
	private void displayMenuText() {
		System.out.println("================================================");
		System.out.println("Welcome to Revature FCU Account Mangement System");
		System.out.println("================================================");
		System.out.println("Please select an option: ");
		System.out.println();
		System.out.println("ViewAccounts: View a list of all accounts");
		System.out.println("ViewCustomers: View a list of all customers");
		System.out.println("ViewAccountInfo: View a summary of the chosen customer's accounts");
		System.out.println("NewCustomer: Creates a new customer");
		System.out.println("OpenAccount: Create a new account for a customer");
		System.out.println("CloseAccounts: Closes all accounts for a customer");
		System.out.println("InternalTransfer: Transfers money between RFCU accounts");
		System.out.println("Withdrawal: Withdraws money from the chosen account");
		System.out.println("Deposit: Deposits money to the chosen account");
		System.out.println("Exit: terminates Account Management System");
	} //end displayMenuText();
}
//TODO: Add input cleaning function