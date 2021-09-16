package com.revature.models;

public class Customer {
	
	//instance variables
	private int customer_id;
	private String f_name;
	private String l_name;
	
	//ctors
	public Customer() {
		// unused
	}//end ctor

	public Customer(int customer_id, String f_name, String l_name) {
		super();
		this.customer_id = customer_id;
		this.f_name = f_name;
		this.l_name = l_name;
	}//end ctor
	
	public Customer(String f_name, String l_name) {
		super();
		this.f_name = f_name;
		this.l_name = l_name;
	}//end ctor

	
	//instance methods
	
	//boilerplate instance methods
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + customer_id;
		result = prime * result + ((f_name == null) ? 0 : f_name.hashCode());
		result = prime * result + ((l_name == null) ? 0 : l_name.hashCode());
		return result;
	}//end func
	

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Customer other = (Customer) obj;
		if (customer_id != other.customer_id)
			return false;
		if (f_name == null) {
			if (other.f_name != null)
				return false;
		} else if (!f_name.equals(other.f_name))
			return false;
		if (l_name == null) {
			if (other.l_name != null)
				return false;
		} else if (!l_name.equals(other.l_name))
			return false;
		return true;
	}//end func

	
	@Override
	public String toString() {
		return "Customer [customer_id=" + customer_id + ", f_name=" + f_name + ", l_name=" + l_name + "]";
	}//end func
	
	//getters/setters

	/**
	 * @return the customer_id
	 */
	public int getCustomer_id() {
		return customer_id;
	}

	/**
	 * @param customer_id the customer_id to set
	 */
	void setCustomer_id(int customer_id) {
		this.customer_id = customer_id;
	}

	/**
	 * @return the f_name
	 */
	public String getF_name() {
		return f_name;
	}

	/**
	 * @param f_name the f_name to set
	 */
	void setF_name(String f_name) {
		this.f_name = f_name;
	}

	/**
	 * @return the l_name
	 */
	public String getL_name() {
		return l_name;
	}

	/**
	 * @param l_name the l_name to set
	 */
	void setL_name(String l_name) {
		this.l_name = l_name;
	}
	
	//getters/setters
	
}//end class
