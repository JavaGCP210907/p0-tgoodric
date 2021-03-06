package com.revature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
			String sql = "select * from accounts order by account_id asc";

			Statement s = conn.createStatement();
			rs = s.executeQuery(sql);
			return generateResults(rs);

		}
		catch(SQLException e) {
			throw new SQLException("Account list retrieval failed", e.getSQLState());
		}
	}

	@Override
	public void addAccount(Account account) {
		try(Connection conn = ConnectionUtil.getConnection()){

			String sql = "insert into accounts (customer_id_fk, account_type, balance)" +
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
	
	public double getBalance(int accountId) throws SQLException {
		try(Connection conn = ConnectionUtil.getConnection()){
			ResultSet rs = null;
			String sql = "select * from accounts where account_id = ?";
			
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, accountId);
			
			rs = ps.executeQuery();
			ArrayList<Account> results = generateResults(rs);
			return results.get(0).getBalance();
		}
		catch(SQLException e) {
			throw new SQLException("Error accessing account balance", e.getSQLState());
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
			//double balance = 
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
	public ArrayList<Account> getAccounts(int customer_id_fk) throws SQLException { 
		//completed
		try(Connection conn = ConnectionUtil.getConnection()){
			ResultSet rs = null;

			String sql = "select * from accounts where customer_id_fk = ?";

			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, customer_id_fk);

			rs = ps.executeQuery();

			ArrayList<Account> accounts = generateResults(rs);

			return accounts;
		}
		catch(SQLException e) {
			throw new SQLException("Account data retrieval failed: ", e.getSQLState());
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
					rs.getInt("account_id"), 	 //this is called account_id, not account_number, 
					rs.getInt("customer_id_fk"), //you utter bonehead
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
		ArrayList<Account> accountsRemoved = getAccounts(customer_id_fk);
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

	public ArrayList<Account> getAccounts(String f_name, String l_name) throws SQLException {
		try(Connection conn = ConnectionUtil.getConnection()){
			ResultSet rs = null;
			ArrayList<Account> result = null;
			String sql = "select * from accounts inner join customers on " + 
					"customers.customer_id = accounts.customer_id_fk " +
					"where f_name = ? and l_name = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, f_name);
			ps.setString(2, l_name);
			rs = ps.executeQuery();

			result = generateResults(rs);
			return result;
		}
		catch(SQLException e) {
			throw new SQLException("Failed to locate accounts for customer", e.getSQLState());
		}


	}
	public int getAccountId(ArrayList<Account> accounts) {
		//return accounts.
		return 0;
	}
}
