package com.revature.dao;

import java.sql.SQLException;
import java.util.ArrayList;

import com.revature.models.Account;

public interface IAccountDao {
	
	public ArrayList<Account> getAccounts() throws SQLException; //retrieves all
	
	public void addAccount(Account account);
	
	public void alterBalance(int account_id, double amount) throws SQLException;
	
	public ArrayList<Account> getAccountsByCustomerId(int customer_id) throws SQLException;
	
	public void removeAccountByCustomerId(int customer_id) throws SQLException;
	
	//public List<Account> getAccountsByName
}
