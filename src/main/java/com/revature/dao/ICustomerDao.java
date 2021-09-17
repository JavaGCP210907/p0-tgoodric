package com.revature.dao;

//imports
import java.util.List;

import com.revature.models.Customer;

public interface ICustomerDao {
	
	public void createCustomer();
	
	public void closeAccount(int customer_id);
	
	public List<Customer> getCustomers();
	
	public List<Customer> getCustomerByID();
}
