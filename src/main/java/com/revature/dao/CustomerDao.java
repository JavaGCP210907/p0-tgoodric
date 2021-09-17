package com.revature.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.revature.models.Customer;
import com.revature.utils.ConnectionUtil;

public class CustomerDao implements ICustomerDao {

	public CustomerDao() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createCustomer() {
		// TODO Auto-generated method stub

	}

	@Override
	public void closeAccount(int customer_id) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Customer> getCustomers() {
		
		//open connection
		try(Connection conn = ConnectionUtil.getConnection()){
			
			//initialize result set
			ResultSet rs = null;
			
			//generate SQL string
			String sql = "select * from customers";
			//generate statement and execute query
			Statement s = conn.createStatement();
			rs = s.executeQuery(sql);
			
			//populate list with the results
			List<Customer> customerList = new ArrayList<>();
			
			Customer cust = null;
			
			while(rs.next()) {
				cust = new Customer(
						rs.getInt("customer_id"),
						rs.getString("f_name"),
						rs.getString("l_name")
						);
						
				customerList.add(cust);
			} //end while
			
			return customerList;
			
		}//end try
		catch(SQLException e) {
			System.out.println("Error with database");
			//TODO: add logging here
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public List<Customer> getCustomerByID() {
		// TODO Auto-generated method stub
		return null;
	}

}