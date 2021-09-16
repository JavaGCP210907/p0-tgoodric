package com.revature.models;

//import statements
import java.util.Scanner;

public class Menu {

	
	public static void runMenu() {
		Scanner sc = new Scanner(System.in);
		boolean done = false;
		while(!done) {
			displayMenuText(); //moved to separate function for aesthetics
			done = menuLogic(sc.nextLine());  //likewise
		}//end while
		sc.close();
	} //end runMenu()
	
	/**
	 * Contains all of the logic for the menu and terminates loop
	 * upon receiving exit command
	 * 
	 * @param selection String
	 * @return boolean
	 */
	private static boolean menuLogic(String selection){
		switch(selection.toLowerCase()) { //TODO: Finish switch statement logic in each case including logging
		case "newcustomer":{
			//execute new customer logic
			break;
		}//end case
		case "openaccount":{
			break;
		}//end case
		case "closeaccount":{
			break;
		}//end case
		case "accountbalance":{
			break;
		}//end case
		case "totalbalance":{
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
			
		}//end case
		default:{
			System.out.println("Command not found");
		}//end default
		}//end switch
		
		return selection.toLowerCase().equals("exit"); //strip spaces, clean input in separate method?
	}//end menuLogic
	
	/**
	 * prints the menu text, moved to separate function
	 * for aesthetic purposes
	 */
	private static void displayMenuText() {
		System.out.println("================================================");
		System.out.println("Welcome to Revature FCU Account Mangement System");
		System.out.println("================================================");
		System.out.println("Please select an option: ");
		System.out.println();
		System.out.println("NewCustomer:   		Creates a new customer");
		System.out.println("OpenAccount:   		Create a new account for a customer");
		System.out.println("CloseAccount:  		Closes all accounts for a customer");
		System.out.println("AccountBalance: 	Displays the balance of the customer's chosen account");
		System.out.println("TotalBalance:		Displays the total available in all the customer's accounts");
		System.out.println("InternalTransfer:	Transfers money between RFCU accounts");
		System.out.println("Withdrawal: 		Withdraws money from the chosen account");
		System.out.println("Deposit:            Deposits money to the chosen account");
	} //end displayMenuText();
}
//TODO: Add input cleaning function