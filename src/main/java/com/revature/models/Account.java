package com.revature.models;

public class Account {

	//instance variables
	private int account_number;
	private int customer_id_fk;
	private String account_type;
	private double balance;
	
	
	//ctors
	public Account() {
		//default, unused
	}

	public Account(int account_number, int customer_id_fk, String account_type, double balance) {
		super();
		this.account_number = account_number;
		this.customer_id_fk = customer_id_fk;
		this.account_type = account_type;
		this.balance = balance;
	}
	
	public Account(int customer_id_fk, String account_type, double balance) {
		super();
		this.customer_id_fk = customer_id_fk;
		this.account_type = account_type;
		this.balance = balance;
	}
	
	//instance methods
	
	//boilerplate methods
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + account_number;
		result = prime * result + ((account_type == null) ? 0 : account_type.hashCode());
		long temp;
		temp = Double.doubleToLongBits(balance);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + customer_id_fk;
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
		Account other = (Account) obj;
		if (account_number != other.account_number)
			return false;
		if (account_type == null) {
			if (other.account_type != null)
				return false;
		} else if (!account_type.equals(other.account_type))
			return false;
		if (Double.doubleToLongBits(balance) != Double.doubleToLongBits(other.balance))
			return false;
		if (customer_id_fk != other.customer_id_fk)
			return false;
		return true;
	}
	
	
	//getters and setters
	/**
	 * @return the account_number
	 */
	public int getAccount_number() {
		return account_number;
	}

	/**
	 * @param account_number the account_number to set
	 */
	void setAccount_number(int account_number) {
		this.account_number = account_number;
	}

	/**
	 * @return the customer_id_fk
	 */
	public int getCustomer_id_fk() {
		return customer_id_fk;
	}

	/**
	 * @param customer_id_fk the customer_id_fk to set
	 */
	void setCustomer_id_fk(int customer_id_fk) {
		this.customer_id_fk = customer_id_fk;
	}

	/**
	 * @return the account_type
	 */
	public String getAccount_type() {
		return account_type;
	}

	/**
	 * @param account_type the account_type to set
	 */
	void setAccount_type(String account_type) {
		this.account_type = account_type;
	}

	/**
	 * @return the balance
	 */
	public double getBalance() {
		return balance;
	}

	/**
	 * @param balance the balance to set
	 */
	void setBalance(double balance) {
		this.balance = balance;
	}
	
	
}//end class
