package com.revature.models;

public class Customer {
	
	//instance variables
	private int customer_id;
	private String f_name;
	private String l_name;
	private String street_address;
	private String city;
	private String state;
	
	//ctors
	public Customer() {
		// unused
	}//end ctor

	public Customer(String f_name, String l_name, String street_address, String city, String state) {
		super();
		this.f_name = f_name;
		this.l_name = l_name;
		this.street_address = street_address;
		this.city = city;
		this.state = state;
	}

	public Customer(int customer_id, String f_name, String l_name, String street_address, String city, String state) {
		super();
		this.customer_id = customer_id;
		this.f_name = f_name;
		this.l_name = l_name;
		this.street_address = street_address;
		this.city = city;
		this.state = state;
	}

	@Override
	public String toString() {
		return "Customer [customer_id=" + customer_id + ", f_name=" + f_name + ", l_name=" + l_name
				+ ", street_address=" + street_address + ", city=" + city + ", state=" + state + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + customer_id;
		result = prime * result + ((f_name == null) ? 0 : f_name.hashCode());
		result = prime * result + ((l_name == null) ? 0 : l_name.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		result = prime * result + ((street_address == null) ? 0 : street_address.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Customer other = (Customer) obj;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
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
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		if (street_address == null) {
			if (other.street_address != null)
				return false;
		} else if (!street_address.equals(other.street_address))
			return false;
		return true;
	}

	/**
	 * @return the customer_id
	 */
	public int getCustomer_id() {
		return customer_id;
	}

	/**
	 * @return the f_name
	 */
	public String getF_name() {
		return f_name;
	}

	/**
	 * @return the l_name
	 */
	public String getL_name() {
		return l_name;
	}

	/**
	 * @return the street_address
	 */
	public String getStreet_address() {
		return street_address;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param customer_id the customer_id to set
	 */
	void setCustomer_id(int customer_id) {
		this.customer_id = customer_id;
	}

	/**
	 * @param f_name the f_name to set
	 */
	void setF_name(String f_name) {
		this.f_name = f_name;
	}

	/**
	 * @param l_name the l_name to set
	 */
	void setL_name(String l_name) {
		this.l_name = l_name;
	}

	/**
	 * @param street_address the street_address to set
	 */
	void setStreet_address(String street_address) {
		this.street_address = street_address;
	}

	/**
	 * @param city the city to set
	 */
	void setCity(String city) {
		this.city = city;
	}

	/**
	 * @param state the state to set
	 */
	void setState(String state) {
		this.state = state;
	}


	
}//end class
