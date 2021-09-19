package com.revature.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import com.revature.models.Customer;

public interface ICustomerDao {
	
	public void createCustomer(Customer customer) throws SQLException;
	
	public void closeAccount(int customer_id) throws SQLException;
	
	public ArrayList<Customer> getCustomers() throws SQLException;
	
	public ArrayList<Customer> getCustomers(int id) throws SQLException;
	
	public ArrayList<Customer> getCustomers(String f_name, String l_name) throws SQLException;
	
	public ArrayList<Customer> getCustomers(String f_name, String l_name, String street_address, 
										   String city, String state) throws SQLException;
}
