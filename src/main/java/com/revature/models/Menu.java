package com.revature.models;

import java.sql.SQLException;
import java.util.ArrayList;
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
		boolean newCustomerFlag = false;
		switch(selection.toLowerCase()) { //TODO: Finish switch statement logic in each case including logging
		case "viewaccounts":{ //COMPLETED 09/18 2:21 PM
			viewAccounts(); //extracted completed function for readability of switch
			break;
		}//end case
		case "viewcustomers":{//COMPLETED 09/17
			viewCustomers();  //extracted for readability
			break;
		}//end case
		case "viewaccountinfo":{ //TODO: Figure out what the hell is going on with this
			System.out.println("Enter the first name of the customer whose account info you want to view: ");
			String f_name = scan.nextLine().strip();
			System.out.println("Enter the last name of the customer whose account info you want to view: ");
			String l_name = scan.nextLine().strip();
			ArrayList<Customer> customers = null;
			ArrayList<Account> accounts = null;
			double totalBalance = 0.0;
			int customer_id_fk = -1; //error code, will be changed if execution successful
			try {
				customers = cDao.getCustomers(f_name, l_name);
				if (customers != null && customers.size() > 0) { 
					customer_id_fk = selectCustomer(customers);
					//TODO: continue wiht implementation of account info viewing. 
					//Print each account for the selected customer
					accounts = aDao.getAccounts(customer_id_fk);
					for (Account account : accounts) {
						System.out.println("Account type: " + account.getAccount_type());
						System.out.printf("Balance: $%.2f", account.getBalance());
						System.out.println();
						totalBalance += account.getBalance();
					}
					System.out.printf("Balance: $%.2f", totalBalance);
					//add balance available for each account to total balance, print
				}
				else {
					System.out.println("No matching customer found.");
				}
			}
			catch(SQLException e) {
				System.out.println("An error occurred while interfacing with the database");
				log.error(e.getMessage() + " " + e.getSQLState());
			}
			break;
		}//end case
		case "newcustomer":{ 
			newCustomer(); //extracted for readability
			break;
		}//end case
		case "openaccount":{
			openAccount();
			break;
		}//end case
		case "closeaccounts":{
			System.out.println("Enter first name of customer to close account");
			String f_name = scan.nextLine();
			System.out.println("Enter last name of customer to close account");
			String l_name = scan.nextLine();
			ArrayList<Customer> results = null;
			try {
				results = cDao.getCustomers(f_name, l_name);
			} catch (SQLException e) {
				log.error(e.getMessage() + e.getSQLState());
			}
			int choice = selectCustomer(results);
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
			System.out.println("Which customer is withdrawing from their account?");
			viewCustomers();

			break;
		}
		case "deposit":{
			//TODO: implement withdrawal function
			break;
		}//end case
		case "exit":{
			System.out.println("Terminating program.");
			break;
		}//end case
		case "sandbox":{
			System.out.println("searching by all params");
			try {
				ArrayList<Account> result = aDao.getAccounts();

				System.out.println(result);
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
	 * 
	 */
	private void openAccount() {
		System.out.println("Enter first name of customer opening account");
		String f_name = scan.nextLine().strip();
		System.out.println("Enter last name of customer opening account");
		String l_name = scan.nextLine().strip();
		ArrayList<Customer> customers = null;
		int customer_id_fk;
		try {
			customers = cDao.getCustomers(f_name, l_name);
		} catch (SQLException e) {
			log.error(e.getMessage() + " " + e.getSQLState());
			e.printStackTrace();
		}

		customer_id_fk = selectCustomer(customers);
		openAccount(customer_id_fk);
	}

	/**
	 * @param customer_id_fk
	 */
	private void openAccount(int customer_id_fk) {
		System.out.println("Enter type of account: ");
		String account_type = scan.nextLine().strip();
		System.out.println("Enter initial balance: ");
		double balance = parseDecimalInput();

		aDao.addAccount(new Account(customer_id_fk, account_type, balance));
	}

	/**
	 * Creates a new customer, if unique data is entered
	 */
	private void newCustomer() {
		System.out.println("Enter new customer's first name: ");
		String f_name = scan.nextLine().strip();
		System.out.println("Enter new customer's last name: ");
		String l_name = scan.nextLine().strip();
		System.out.println("Enter new customer's street address: ");
		String street_address = scan.nextLine().strip();
		System.out.println("Enter new customer's city: ");
		String city = scan.nextLine().strip();
		System.out.println("Enter new customer's state: ");
		String state = scan.nextLine().strip();
		//System.out.println("");
		try {
			ArrayList<Customer> duplicates = cDao.getCustomers(f_name, l_name, street_address, city, state);
			if(duplicates.size() > 0) {
				System.out.println("An account already exists for " + f_name + " " + l_name);
				log.warn("User attempted to create a duplicate account: " + f_name + " " + l_name);
			}
			else {
				cDao.createCustomer(new Customer(f_name, l_name, street_address, city, state));
				log.info("User created new customer: " + f_name + " " + l_name); 
			}
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage() + e.getSQLState());
		}//end try/catch
		
		
	}

	/**
	 * Displays a list of all accounts
	 */
	private void viewAccounts() {
		ArrayList<Account> accounts;
		try {
			accounts = aDao.getAccounts();
			viewAccounts(accounts);
		} catch (SQLException e) {
			System.out.println("An error occured while accessing database");
			log.error(e.getMessage() + e.getSQLState());
			e.printStackTrace();
		}//end try/catch
	}

	/**
	 * @param customers the list of customers to be selected from
	 * @return int The customer_id of the selected customer
	 */
	private int selectCustomer(ArrayList<Customer> customers) {
		int customer_id = -1; //error code
		if ((customers != null) && (customers.size() > 0)) {
			if(customers.size() == 1) {
				customer_id = customers.get(0).getCustomer_id();
			}
			else {
				System.out.println("Select customer from the following:");
				int selection;
				for (int i = 0; i < customers.size(); i++) {
					System.out.println(i + ": " + customers.get(i));
				}
				boolean valid = false;
				do {
					selection = parseIntegerInput();
					valid = selection < customers.size();
					if(!valid) {
						System.out.println("Invalid selection. Please choose a customer from the list.");
					}
				} while(!valid);
				customer_id = customers.get(selection).getCustomer_id();
			}
		}
		return customer_id;
	}

	/**
	 * @return double The value parsed from the user input
	 */
	private double parseDecimalInput() {
		boolean valid = false;
		double value = 0.0;
		do {
			try {
				value = Double.parseDouble(scan.nextLine());
				if(value >= 0.0) {
					valid = true;
				}
				else {
					System.out.println("enter a nonnegative value");
				}
			}
			catch (NumberFormatException e) {
				System.out.println("Please enter a numeric value.");
			}
		} while(!valid);

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
	private int parseIntegerInput() {
		boolean valid = false;
		int value = 0;
		do {
			try {
				value = Integer.parseInt(scan.nextLine());
				if(value >= 0) {
					valid = true;
				}
			}
			catch (NumberFormatException e) {
				System.out.println("Please enter a numeric value.");
			}
		} while(!valid);
		if (value < 0) {
			return -1;
		} //don't think I need this, scared to remove it anyways
		return value;
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