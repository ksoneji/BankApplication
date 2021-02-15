package com.bank.model.dao;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
/**
 * @author Ketan.Soneji
 */
@Entity
@Table(name = "accounts")
public class Account implements java.io.Serializable {

	private static final long serialVersionUID = 4910225916550731449L;
	
	private long id;
	private Customer customer;
	private String accountType;
	private boolean isActive = true;
	private Timestamp createdDate;
	private Double interestRate = 3.5;
	private Timestamp interestCalculatedDate;
	
	public Account() {
	}

	public Account(long id, Customer customer, String accountType, boolean isActive, Timestamp createdDate, Timestamp interestCalculatedDate, Double interestRate) {
		this.id = id;
		this.customer = customer;
		this.accountType = accountType;
		this.isActive = isActive;
		this.createdDate = createdDate;
		this.interestCalculatedDate = interestCalculatedDate;
		this.interestRate = interestRate;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_id", nullable = false)
	public Customer getCustomer() {
		return this.customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@Column(name = "account_type", nullable = false, length = 7)
	public String getAccountType() {
		return this.accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	@Column(name = "created_date", nullable = false, insertable = false, updatable = false)
	@CreationTimestamp
	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	@Column(name = "status", columnDefinition="tinyint(1) default 1")
	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	@Column(name = "interest_calculated_date")
	public Timestamp getInterestCalculatedDate() {
		return interestCalculatedDate;
	}

	public void setInterestCalculatedDate(Timestamp interestCalculatedDate) {
		this.interestCalculatedDate = interestCalculatedDate;
	}

	@Column(name = "interest_rate")
	public Double getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(Double interestRate) {
		this.interestRate = interestRate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accountType == null) ? 0 : accountType.hashCode());
		result = prime * result + ((createdDate == null) ? 0 : createdDate.hashCode());
		result = prime * result + ((customer == null) ? 0 : customer.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((interestCalculatedDate == null) ? 0 : interestCalculatedDate.hashCode());
		result = prime * result + ((interestRate == null) ? 0 : interestRate.hashCode());
		result = prime * result + (isActive ? 1231 : 1237);
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
		if (accountType == null) {
			if (other.accountType != null)
				return false;
		} else if (!accountType.equals(other.accountType))
			return false;
		if (createdDate == null) {
			if (other.createdDate != null)
				return false;
		} else if (!createdDate.equals(other.createdDate))
			return false;
		if (customer == null) {
			if (other.customer != null)
				return false;
		} else if (!customer.equals(other.customer))
			return false;
		if (id != other.id)
			return false;
		if (interestCalculatedDate == null) {
			if (other.interestCalculatedDate != null)
				return false;
		} else if (!interestCalculatedDate.equals(other.interestCalculatedDate))
			return false;
		if (interestRate == null) {
			if (other.interestRate != null)
				return false;
		} else if (!interestRate.equals(other.interestRate))
			return false;
		if (isActive != other.isActive)
			return false;
		return true;
	}

}
