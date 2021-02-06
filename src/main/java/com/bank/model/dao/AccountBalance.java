package com.bank.model.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "account_balance")
public class AccountBalance implements java.io.Serializable {

	private static final long serialVersionUID = 4910225916550731486L;
	
	private long id;
	private long accountId;
	private double credit;
	private double debit;
	private double balance;
	private long date;

	public AccountBalance() {
	}

	public AccountBalance(long id, long accountId, double balance, long date) {
		this.id = id;
		this.accountId = accountId;
		this.balance = balance;
		this.date = date;
	}

	public AccountBalance(long id, long accountId, double credit, double debit, double balance, long date) {
		this.id = id;
		this.accountId = accountId;
		this.credit = credit;
		this.debit = debit;
		this.balance = balance;
		this.date = date;
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

	@Column(name = "account_id")
	public long getAccountId() {
		return this.accountId;
	}

	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}

	@Column(name = "credit", nullable = true)
	public double getCredit() {
		return this.credit;
	}

	public void setCredit(double credit) {
		this.credit = credit;
	}

	@Column(name = "debit", nullable = true)
	public double getDebit() {
		return this.debit;
	}

	public void setDebit(double debit) {
		this.debit = debit;
	}

	@Column(name = "balance", nullable = false)
	public double getBalance() {
		return this.balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	@Column(name = "date", nullable = false)
	public long getDate() {
		return this.date;
	}

	public void setDate(long date) {
		this.date = date;
	}

}
