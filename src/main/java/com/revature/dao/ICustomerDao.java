package com.revature.dao;

import java.sql.SQLException;
//imports
import java.util.List;

import com.revature.models.Customer;

public interface ICustomerDao {
	
	public void createCustomer(Customer customer) throws SQLException;
	
	public void closeAccount(int customer_id) throws SQLException;
	
	public List<Customer> getCustomers();
	
	public List<Customer> getCustomerByID();
}
