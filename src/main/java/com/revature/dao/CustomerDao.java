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
			String sql = "insert into customers(f_name, l_name) " +
						 "values (?, ?)";
			
			PreparedStatement ps = conn.prepareStatement(sql);
			
			ps.setString(1, customer.getF_name());
			ps.setString(2, customer.getL_name());
			
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
			
			/*
			String sql = "delete from accounts where customer_id_fk = ?";
			
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, customer_id);
			*/
			
			
			String sql = "delete from customers where customer_id = ?";
			
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, customer_id);
			
			ps.executeUpdate();
		}
		catch (SQLException e) {
			throw new SQLException("Failed to remove customer: Accounts still open", e.getSQLState());
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
			String sql = "select * from customers";
			//generate statement and execute query
			Statement s = conn.createStatement();
			rs = s.executeQuery(sql);
			
			//populate list with the results
			ArrayList<Customer> customerList = generateResults(rs);
			
			return customerList;
			
		}//end try
		catch(SQLException e) {
			//completed: per decision 09/18 all exceptions in DAO layer are
			//to be re-thrown with message for logger
			throw new SQLException("Error accessing customers table"); 
		}
	}

	@Override
	public ArrayList<Customer> getCustomerByID(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Customer> getCustomersByName(String f_name, String l_name) throws SQLException {
		
		try(Connection conn = ConnectionUtil.getConnection()){
			//set up necessary resources
			
			ResultSet rs = null;
			String sql = "select * from accounts where (f_name = ?) and (l_name = ?)";
			PreparedStatement ps = conn.prepareStatement(sql);
			
			//generate and execute query
			ps.setString(1, f_name);
			ps.setString(2, l_name);
			
			rs = ps.executeQuery();
			
			//populate list
			return generateResults(rs);
			
		}
		catch (SQLException e) {
			throw new SQLException("Customer " + f_name +" "+ l_name + " not found");
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
					rs.getString("l_name")
					);
			customers.add(c);
		}
		return customers;
	}
	
}
