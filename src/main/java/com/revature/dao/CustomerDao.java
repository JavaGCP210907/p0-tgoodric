package com.revature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.revature.models.Customer;
import com.revature.utils.ConnectionUtil;

public class CustomerDao implements ICustomerDao {

	public CustomerDao() {
		// no implementation
	}

	@Override
	/**
	 * Adds a new customer to the database
	 * 
	 * @param Customer the customer to be added to the database
	 */
	public void createCustomer(Customer customer) throws SQLException {
		try(Connection conn = ConnectionUtil.getConnection()){
			String sql = "insert into customers(f_name, l_name, street_address, city, state) " + //finish writing the damned
						 "values (?, ?, ?, ?, ?)";												 //SQL query next time,
																								 //you utter bonehead
			PreparedStatement ps = conn.prepareStatement(sql);
			
			ps.setString(1, customer.getF_name());
			ps.setString(2, customer.getL_name());
			ps.setString(3, customer.getStreet_address());
			ps.setString(4, customer.getCity());
			ps.setString(5, customer.getState());
			
			ps.executeUpdate();
			
		}
		catch(SQLException e) {
			throw new SQLException("Customer creation failed: " + customer.getF_name() + customer.getL_name());
		}
	}//completed

	@Override
	public void closeAccount(int customer_id) throws SQLException {
		// TODO Auto-generated method stub
		try(Connection conn = ConnectionUtil.getConnection()){
			String sql = "delete from accounts where customer_id_fk = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, customer_id);
			ps.executeUpdate();
			sql = "delete from customers where customer_id = ?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, customer_id);
			ps.executeUpdate();
		}
		catch (SQLException e) {
			throw new SQLException("Failed to remove customer: Accounts still open", e.getSQLState()); //probably
			//Completed 09/18, exception rethrown
		}
	}

	@Override
	public ArrayList<Customer> getCustomers() throws SQLException {
		
		//open connection
		try(Connection conn = ConnectionUtil.getConnection()){
			
			//initialize result set
			ResultSet rs = null;
			//generate sql string
			String sql = "select * from customers order by customer_id asc";
			//generate statement and execute query
			Statement s = conn.createStatement();
			rs = s.executeQuery(sql);
			
			//populate list with the results
			return generateResults(rs);
			
			 
			
		}//end try
		catch(SQLException e) {
			//completed: per decision 09/18 all exceptions in DAO layer are
			//to be re-thrown with message for logger
			throw new SQLException("Error accessing customers table", e.getSQLState()); 
		}
	}

	@Override
	public ArrayList<Customer> getCustomers(int customer_id) throws SQLException {
		try(Connection conn = ConnectionUtil.getConnection()){
			ResultSet rs = null;
			String sql = "select * from customers where customer_id = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, customer_id);
			
			rs = ps.executeQuery();
			return generateResults(rs);
		}
		catch (SQLException e) {
			throw new SQLException("Customer retrieval failed check logic in getCustomers", e.getSQLState());
		}
	}

	@Override
	public ArrayList<Customer> getCustomers(String f_name, String l_name) throws SQLException {
		
		try(Connection conn = ConnectionUtil.getConnection()){
			//set up necessary resources
			
			ResultSet rs = null;
			String sql = "select * from customers where f_name = ? and l_name = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			
			//generate and execute query
			ps.setString(1, f_name);
			ps.setString(2, l_name);
			
			rs = ps.executeQuery();
			
			//populate list
			return generateResults(rs);
			
		}
		catch (SQLException e) {
			throw new SQLException("Customer " + f_name +" "+ l_name + " not found", e.getSQLState());
		}
	}

	/**
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private ArrayList<Customer> generateResults(ResultSet rs) throws SQLException {
		ArrayList<Customer> customers = new ArrayList<>();
		while(rs.next()) {
			Customer c = new Customer(
					rs.getInt("customer_id"),
					rs.getString("f_name"),
					rs.getString("l_name"),
					rs.getString("street_address"),
					rs.getString("city"),
					rs.getString("state")
					);
			customers.add(c);
		}
		return customers;
	}

	@Override
	public ArrayList<Customer> getCustomers(String f_name, String l_name, String street_address, String city,
			String state) throws SQLException {
		try(Connection conn = ConnectionUtil.getConnection()){
			ResultSet rs = null;
			String sql = "select * from customers where f_name = ? and l_name = ? "+
						 "and street_address = ? and city = ? and state = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, f_name);
			ps.setString(2, l_name);
			ps.setString(3, street_address);
			ps.setString(4, city);
			ps.setString(5, state);
			
			rs = ps.executeQuery();
			return generateResults(rs);
		}
		catch (SQLException e) {
			throw new SQLException("Customer retrieval failed", e.getSQLState());
		}
	}
	public int getCustomerId(String f_name, String l_name, String street_address, String city,
			String state) throws SQLException {
		try {
			Customer c = getCustomers(f_name, l_name, street_address, city, state).get(0);
			return c.getCustomer_id();
		} catch (SQLException e) {
			throw new SQLException("Failed to fetch customer id", e.getSQLState());
		}
		catch(IndexOutOfBoundsException e) {
			System.out.println("If you are seeing this, fix your code on the getCustomerID function");
			return 0;
		}
	}
}
