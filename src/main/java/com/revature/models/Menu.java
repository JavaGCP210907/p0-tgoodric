package com.revature.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.dao.AccountDao;
import com.revature.dao.CustomerDao;
import com.revature.utils.ConnectionUtil;

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
	 * @return boolean used to terminate while loop
	 */
	private boolean menuLogic(String selection){ //brace yourself, this is ugly
		switch(selection.toLowerCase()) { //TODO: Finish switch statement logic in each case including logging
		case "viewaccounts":{
			viewAccounts();
			break;
		}//end case
		case "viewcustomers":{
			viewCustomers(); //
			break;
		}//end case
		case "viewaccountinfo":{
			System.out.println("Select customer: ");
			System.out.println();
			viewCustomers();
			//boolean successful = false;
			int choice = parseCustomerId();
			//select
			if (choice != -1) {
				try(Connection conn = ConnectionUtil.getConnection()){
					String sql = "delete from customers where customer_id = ?";
					
					PreparedStatement ps = conn.prepareStatement(sql);
					ps.setInt(1, choice);
					
				}
				catch (SQLException e) {
					log.error("Failure to remove customer: " + choice);
				}
			}
			break;
		}//end case
		case "viewcustomerinfo":{ //prints basic customer information, account info, balance
			System.out.println("customerv");
			break;
		}//end case
		case "newcustomer":{
			//execute new customer logic
			log.info("User created new customer: "); //TODO: add customer name, id
			//break;
		}//end case, fall-through deliberate
		case "openaccount":{
			
			break;
		}//end case
		case "closeaccounts":{
			System.out.println("Select customer account to close");
			int custIdNumber = 0;
			cDao.closeAccount(custIdNumber);
			log.warn("Customer " + custIdNumber + " accounts closed");
			break;
		}//end case
		case "internaltransfer":{
			break;
		}//end case
		case "withdrawal":{
			
			break;
		}
		case "deposit":{
			break;
		}//end case
		case "exit":{
			break;
		}//end case
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
	private void viewAccounts() {
		List<Account> accounts = aDao.getAccounts();
		for (Account a : accounts) {
			System.out.println(a);
		}
		
		System.out.println();
	}

	/**
	 * Parses out customer ID
	 * @return int customer id number, or -1 if error occurs
	 */
	private int parseCustomerId() {
		int choice;
		try {
				choice = scan.nextInt();
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
	 * Displays list of customers
	 */
	private void viewCustomers() {
		List<Customer> customers = cDao.getCustomers();
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
		System.out.println("ViewAccountInfo: View a summary of the chosen account");
		System.out.println("ViewCustomerInfo: View a summary of a customer's information");
		System.out.println("NewCustomer: Creates a new customer");
		System.out.println("OpenAccount: Create a new account for a customer");
		System.out.println("CloseAccounts: Closes all accounts for a customer");
		System.out.println("InternalTransfer: Transfers money between RFCU accounts");
		System.out.println("Withdrawal: Withdraws money from the chosen account");
		System.out.println("Deposit: Deposits money to the chosen account");
	} //end displayMenuText();
}
//TODO: Add input cleaning function