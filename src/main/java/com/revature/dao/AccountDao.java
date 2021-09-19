package com.revature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.revature.models.Account;
import com.revature.utils.ConnectionUtil;

public class AccountDao implements IAccountDao {

	public AccountDao() {
		// not explicitly used
	}

	@Override
	public ArrayList<Account> getAccounts() throws SQLException{
		// completed
		try(Connection conn = ConnectionUtil.getConnection()){
			
			ResultSet rs = null;
			String sql = "select * from accounts";
			
			PreparedStatement ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			return generateResults(rs);
			
		}
		catch(SQLException e) {
			throw new SQLException("Account list retrieval failed", e.getSQLState());
		}
	}

	@Override
	public void addAccount(Account account) {
		try(Connection conn = ConnectionUtil.getConnection()){
			
			String sql = "insert into employees (customer_id_fk, account_type, balance)" +
						 "values (?, ?, ?)";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, account.getCustomer_id_fk());
			ps.setString(2, account.getAccount_type());
			ps.setDouble(3, account.getBalance());
			
			ps.executeUpdate();
		}
		catch(SQLException e) {
			System.out.println("Account creation failed");
			e.printStackTrace();
		}
	}
	
	/**
	 * Method used to alter the balance for the target account
	 * @param account_id int The target account's unique identifier
	 * @param amount double the amount to adjust the existing balance by
	 * @throws SQLException, probably
	 */
	@Override
	public void alterBalance(int account_id, double amount) throws SQLException { //completed
		try(Connection conn = ConnectionUtil.getConnection()){
			String sql = "update accounts set balance = balance + ? where account_id = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setDouble(1, amount);
			ps.setInt(2, account_id);
			ps.executeUpdate();
		}
		catch(SQLException e) {
			throw new SQLException("Balance alteration failed", e.getSQLState());
		}
	}

	@Override
	public ArrayList<Account> getAccountsByCustomerId(int customer_id) throws SQLException { 
		//completed
		try(Connection conn = ConnectionUtil.getConnection()){
			ResultSet rs = null;

			String sql = "select * from accounts where customer_id_fk = ?";
			
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, customer_id);
			rs = ps.executeQuery();
			
			ArrayList<Account> accounts = generateResults(rs);
			
			return accounts;
		}
		catch(SQLException e) {
			throw new SQLException("Account data retrieval failed", e.getSQLState());
		}
	}

	/**
	 * @param rs The ResultSet used to generate the results
	 * @return ArrayList<Account> 
	 * @throws SQLException
	 */
	private ArrayList<Account> generateResults(ResultSet rs) throws SQLException {
		Account a = null;
		ArrayList<Account> accounts = new ArrayList<>();
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

	@Override
	public void removeAccountsByCustomerId(int customer_id_fk) throws SQLException {
		//completed
		ArrayList<Account> accountsRemoved = getAccountsByCustomerId(customer_id_fk);
		for (Account account : accountsRemoved) {
			removeAccount(account.getAccount_id());
		}
	}

	@Override
	public void removeAccount(int account_id) throws SQLException {
		
		try(Connection conn = ConnectionUtil.getConnection()) {
			String sql = "delete from accounts where account_id = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, account_id);
			ps.executeUpdate();
			
		} catch (SQLException e) {
			throw new SQLException("Account closing failed", e.getSQLState());
		}
		
	}
	
	

}
