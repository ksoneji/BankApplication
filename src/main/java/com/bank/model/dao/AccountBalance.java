package com.bank.model.dao;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "account_balance")
public class AccountBalance implements java.io.Serializable {

	private static final long serialVersionUID = 4910225916550731486L;
	
	private Long id;
	private Long accountId;
	private Double credit;
	private Double debit;
	private Double balance;
	private Timestamp date;

	public AccountBalance() {
	}

	public AccountBalance(Long id, Long accountId, Double balance, Timestamp date) {
		this.id = id;
		this.accountId = accountId;
		this.balance = balance;
		this.date = date;
	}

	public AccountBalance(Long id, Long accountId, Double credit, Double debit, Double balance, Timestamp date) {
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
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "account_id")
	public Long getAccountId() {
		return this.accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	@Column(name = "credit", nullable = true)
	public Double getCredit() {
		return this.credit;
	}

	public void setCredit(Double credit) {
		this.credit = credit;
	}

	@Column(name = "debit", nullable = true)
	public Double getDebit() {
		return this.debit;
	}

	public void setDebit(Double debit) {
		this.debit = debit;
	}

	@Column(name = "balance", nullable = false)
	public Double getBalance() {
		return this.balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	@Column(name = "date", nullable = false, insertable = false, updatable = false)
	@CreationTimestamp
	public Timestamp getDate() {
		return this.date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

}
