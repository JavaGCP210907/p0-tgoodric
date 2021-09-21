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
	private boolean menuLogic(String selection){
		//boolean newCustomerFlag = false;
		switch(selection.toLowerCase()) { //TODO: Test all the functions
		case "viewaccounts":{ //COMPLETED 09/18 2:21 PM
			viewAccounts(); //extracted completed function for readability of switch
			break; //TODO: hit the toString() function to format the values to 2 decimal places
		}//end case
		case "viewcustomers":{//COMPLETED 09/17
			viewCustomers(); //extracted for readability
			break;
		}//end case
		case "viewaccountinfo":{ 
			viewAccountInfo(); //Extracted for readability
			break;
		}//end case
		case "newcustomer":{ 
			newCustomer(); //extracted for readability
			break;
		}//end case
		case "openaccount":{
			openAccount(); //extracted for readability
			break;
		}//end case
		case "closeaccounts":{
			closeAccounts();
			break;
		}//end case
		case "internaltransfer":{
			balanceTransfer(); //extracted for readability
			break;
		}//end case
		case "withdrawal":{
			withdrawal(); //extracted for readability
			break;
		}
		case "deposit":{
			deposit(); //extracted for readability
			break;
		}//end case
		case "exit":{
			System.out.println("Terminating program.");
			break;
		}//end case
		case "sandbox":{
			
			try {
				System.out.println(aDao.getBalance(1,1));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		default:{
			System.out.println("Command \"" + selection + "\" not found");
			System.out.println();
		}//end default
		}//end switch

		return selection.toLowerCase().strip().equals("exit");
	}//end menuLogic

	/**
	 * Wrapper function for deposits, extracted for aesthetics
	 */
	private void deposit() {
		System.out.println("Which customer is depositing money?");
		viewCustomers();
		System.out.println("Enter customer first name: ");
		String fName = scan.nextLine().strip();
		System.out.println("Enter customer last name: ");
		String lName = scan.nextLine().strip();
		deposit(fName, lName);
	}

	/**
	 * Wrapper function for withdrawals, extracted for aesthetics
	 */
	private void withdrawal() {
		System.out.println("Which customer is withdrawing money?");
		viewCustomers();
		System.out.println("Enter customer first name: ");
		String fName = scan.nextLine().strip();
		System.out.println("Enter customer last name: ");
		String lName = scan.nextLine().strip();
		withdrawal(fName, lName);
	}

	/**
	 * Wrapper function for internal balance transfers, extracted for aesthetics
	 */
	private void balanceTransfer() {
		System.out.println("Enter first name of source customer: ");
		String sourceFirstName = scan.nextLine();
		System.out.println("Enter last name of source customer: ");
		String sourceLastName = scan.nextLine();	
		
		int sourceId = selectCustomer(sourceFirstName, sourceLastName);
		int sourceAccountId = -1; //error code, will be changed if data retrieval is successful
		try {
			sourceAccountId = selectAccount(aDao.getAccounts(sourceId));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//note no constraints on allowing same customer
		System.out.println("enter first Name of target customer: ");
		String targetFirstName = scan.nextLine();
		System.out.println("Enter last name of source customer: ");
		String targetLastName = scan.nextLine();
		int targetId = selectCustomer(targetFirstName, targetLastName);
		int targetAccountId = -1;
		try {
			targetAccountId = selectAccount(aDao.getAccounts(targetId));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		double balance = 0.0;
		if((sourceAccountId != -1) && (targetAccountId != -1)) {
			System.out.println("Enter balance to be transferred");
			balance = parseDecimalInput();
			try {
				aDao.alterBalance(sourceAccountId, balance * -1.0);
				aDao.alterBalance(targetAccountId, balance);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			log.warn("An invalid customer was selected for an internal transfer");
		}
	}

	/**
	 * Contains the logic for withdrawing funds
	 * @param fName The first name of the customer depositing funds
	 * @param lName The last name of the customer depositing funds
	 */
	private void deposit(String fName, String lName) {
		int customerId = selectCustomer(fName, lName);
		int accountId = -1;
		ArrayList<Account> accounts = null;
		try {
			accounts = aDao.getAccounts(customerId);
			accountId = selectAccount(accounts);
		} 
		catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage() + " " + e.getSQLState());
		}		
		double multiplier = 1.0;
		if((customerId != -1) && (accountId != -1)) {
			alterBalance(customerId, accountId, multiplier);
		}
	}

	/**
	 * closes out all accounts for a customer and removes from database
	 */
	private void closeAccounts() {
		System.out.println("Enter first name of customer to close account");
		String fName = scan.nextLine();
		System.out.println("Enter last name of customer to close account");
		String lName = scan.nextLine();
		int choice = selectCustomer(fName, lName);
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
	}

	/**
	 * Contains the logic for withdrawing funds
	 * @param fName The first name of the person withdrawing money
	 * @param lName The last name of the person withdrawing money
	 */
	private void withdrawal(String fName, String lName) {
		int customerId = selectCustomer(fName, lName);
		int accountId = -1;
		ArrayList<Account> accounts = null;
		try {
			accounts = aDao.getAccounts(customerId);
			accountId = selectAccount(accounts);
		} 
		catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage() + " " + e.getSQLState());
		}
		double multiplier = -1.0;
		if((customerId != -1) && (accountId != -1)) {
			alterBalance(customerId, accountId, multiplier);
		}
	}

	/**
	 * Displays account info for customer
	 */
	private void viewAccountInfo() {
		System.out.println("Enter the first name of the customer whose account info you want to view: ");
		String fName = scan.nextLine().strip();
		System.out.println("Enter the last name of the customer whose account info you want to view: ");
		String lName = scan.nextLine().strip();
		ArrayList<Customer> customers = null;
		ArrayList<Account> accounts = null;
		double totalBalance = 0.0;
		int customerIdFk = -1; //error code, will be changed if execution successful
		try {
			customers = cDao.getCustomers(fName, lName);
			if (customers != null && customers.size() > 0) { 
				customerIdFk = selectCustomer(customers);
				accounts = aDao.getAccounts(customerIdFk);
				for (Account account : accounts) {
					System.out.println("Account type: " + account.getAccount_type());
					System.out.printf("Balance: $%.2f", account.getBalance());
					System.out.println();
					totalBalance += account.getBalance();
				}
				System.out.printf("Total Balance: $%.2f", totalBalance);
				System.out.println();
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
	}

	/**
	 * General function for adjusting balance called by the deposit/withdrawal functions
	 * @param customerId The customer whose account balance is being changed
	 * @param accountId The account whose balance is being changed
	 * @param multiplier either 1 or -1 depending on deposit/withdrawal
	 */
	private boolean alterBalance(int customerId, int accountId, double multiplier) {
		if(accountId != -1) {
			System.out.println("Enter amount: ");
			double amount = parseDecimalInput();

			try {
				   //deposits can't overdraw, withdrawing a smaller amount than balance won't either
				if((multiplier > 0.0 ) || (amount < aDao.getBalance(customerId, accountId))){
								aDao.alterBalance(accountId, (multiplier * amount));
								log.info("Account " + accountId + " balance changed by " + (multiplier * amount));
								return true;
				}
				else {
					log.warn("Customer " + customerId + " attempted to overdraw account " + accountId);
					return false;
				}
			} catch (SQLException e) {
				log.error(e.getMessage() + e.getSQLState());
				e.printStackTrace();
				return false;
			}
		}
		else {
			System.out.println("Account not found.");
			log.warn("User selected invalid account for transaction");
			return false;
		}
	}

	/**
	 * @param fName
	 * @param lName
	 * @return int The customer's ID number
	 */
	private int selectCustomer(String fName, String lName) {
		ArrayList<Customer> results = null;
		try {
			results = cDao.getCustomers(fName, lName);
		} 
		catch (SQLException e) {
			log.error(e.getMessage() + e.getSQLState());
		}
		int choice = selectCustomer(results);
		return choice;
	}

	/**
	 * Prompts user for name to create new account
	 */
	private void openAccount() {
		System.out.println("Enter first name of customer opening account");
		String fName = scan.nextLine().strip();
		System.out.println("Enter last name of customer opening account");
		String lName = scan.nextLine().strip();
		ArrayList<Customer> customers = null;
		int customerIdFk;
		try {
			customers = cDao.getCustomers(fName, lName);
		} catch (SQLException e) {
			log.error(e.getMessage() + " " + e.getSQLState());
			e.printStackTrace();
		}

		customerIdFk = selectCustomer(customers);
		openAccount(customerIdFk);
	}

	/**
	 * Calls the Account DAO to create a new account for the specified customer
	 * @param customerIdFk The customer's ID number
	 */
	private void openAccount(int customerIdFk) {
		System.out.println("Enter type of account: ");
		String account_type = scan.nextLine().strip();
		System.out.println("Enter initial balance: ");
		double balance = parseDecimalInput();

		aDao.addAccount(new Account(customerIdFk, account_type, balance));
	}

	/**
	 * Creates a new customer, if unique data is entered
	 */
	private void newCustomer() {
		System.out.println("Enter new customer's first name: ");
		String fName = scan.nextLine().strip();
		System.out.println("Enter new customer's last name: ");
		String lName = scan.nextLine().strip();
		System.out.println("Enter new customer's street address: ");
		String streetAddress = scan.nextLine().strip();
		System.out.println("Enter new customer's city: ");
		String city = scan.nextLine().strip();
		System.out.println("Enter new customer's state: ");
		String state = scan.nextLine().strip();
		//System.out.println("");
		try {
			ArrayList<Customer> duplicates = cDao.getCustomers(fName, lName, streetAddress, city, state);
			if(duplicates.size() > 0) {
				System.out.println("An account already exists for " + fName + " " + lName);
				log.warn("User attempted to create a duplicate account: " + fName + " " + lName);
			}
			else {
				int newCustomerId = cDao.createCustomer(new Customer(fName, lName, streetAddress, city, state));
				log.info("User created new customer: " + fName + " " + lName); 
				openAccount(newCustomerId);
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
	 * Prompts the user to select a customer from a list of customers
	 * @param customers the list of customers to be selected from
	 * @return int The customer_id of the selected customer
	 */
	private int selectCustomer(ArrayList<Customer> customers) {
		int customerId = -1; //error code
		if ((customers != null) && (customers.size() > 0)) {
			if(customers.size() == 1) {
				customerId = customers.get(0).getCustomer_id();
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
				customerId = customers.get(selection).getCustomer_id();
			}
		}
		return customerId;
	}

	private int selectAccount(ArrayList<Account> accounts) {
		int accountId = -1; //error code
		if ((accounts != null) && (accounts.size() > 0)) {
			if(accounts.size() == 1) {
				accountId = accounts.get(0).getAccount_id();
			}
			else {
				System.out.println("Select account from the following:");
				int selection;
				for (int i = 0; i < accounts.size(); i++) {
					System.out.println(i + ": " + accounts.get(i));
				}
				boolean valid = false;
				do {
					selection = parseIntegerInput();
					valid = selection < accounts.size();
					if(!valid) {
						System.out.println("Invalid selection. Please select an account from the list.");
					}
				} while(!valid);
				accountId = accounts.get(selection).getAccount_id();
			}
		}
		return accountId;
	}

	/**
	 * Prompts the user to enter a value and parses it out.
	 * Repeats until a valid value is passed
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
	 * Prompts user to input an integer value
	 * Repeats until a valid value is passed
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