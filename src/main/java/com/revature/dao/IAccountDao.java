package com.revature.dao;

import java.sql.SQLException;
import java.util.List;

import com.revature.models.Account;

public interface IAccountDao {
	
	public List<Account> getAccounts(); //can be used to return one or many
	
	public void addAccount(Account account);
	
	public void alterBalance(int account_id, double amount);
	
	public List<Account> getAccountsByCustomerId(int customer_id) throws SQLException;
	
	public void removeAccountByCustomerId(int customer_id) throws SQLException;
}
