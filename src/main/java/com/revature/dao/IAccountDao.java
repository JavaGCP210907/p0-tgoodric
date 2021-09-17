package com.revature.dao;

import java.util.List;

import com.revature.models.Account;

public interface IAccountDao {
	
	public List<Account> getAccounts(); //can be used to return one or many
	
	public void addAccount(int customer_id, String account_type, double balance);
	
	public void alterBalance(int account_id, double amount);
	
	public List<Account> getAccountsByCustomerId(int customer_id);
}
