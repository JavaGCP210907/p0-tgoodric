package com.revature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.revature.models.Account;
import com.revature.utils.ConnectionUtil;

public class AccountDao implements IAccountDao {

	public AccountDao() {
		// not explicitly used
	}

	@Override
	public List<Account> getAccounts() {
		// TODO Auto-generated method stub
		
		
		return null;
	}

	@Override
	public void addAccount(int customer_id, String account_type, double balance) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void alterBalance(int account_id, double balance) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Account> getAccountsByCustomerId(int customer_id) {
		// TODO Auto-generated method stub
		try(Connection conn = ConnectionUtil.getConnection()){
			ResultSet rs = null;
			
			//not sure if the join is necessary, could theoretically query on cust_id_fk
			/*
			String sql = "select * from accounts inner join customers "
					   + "on accounts.customer_id_fk = customers.customer_id "
					   + "where accounts.account_id_fk = ?"; //ugly SQL query string
			*/
			String sql = "select * from accounts where account_id_fk = ?";
			
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, customer_id);
			rs = ps.executeQuery();
			
			Account a = null;
			List<Account> accounts = new ArrayList<>();
			while(rs.next()) {
				a = new Account(
					rs.getInt("account_number"),
					rs.getInt("customer_id_fk"),
					rs.getString("account_type"),
					rs.getDouble("balance")
					);
				accounts.add(a);
			}
			
			return accounts;
		}
		catch(SQLException e) {
			
		}
		return null;
	}
	
	

}
