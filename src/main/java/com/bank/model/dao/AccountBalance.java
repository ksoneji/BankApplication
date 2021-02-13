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
	private String comment;
	private Timestamp date;

	public AccountBalance() {
	}

	public AccountBalance(Long id, Long accountId, Double balance, Timestamp date) {
		this.id = id;
		this.accountId = accountId;
		this.balance = balance;
		this.date = date;
	}

	public AccountBalance(Long id, Long accountId, Double credit, Double debit, Double balance, String comment, Timestamp date) {
		this.id = id;
		this.accountId = accountId;
		this.credit = credit;
		this.debit = debit;
		this.balance = balance;
		this.date = date;
		this.comment = comment;
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

	@Column(name = "comment", nullable = true)
	public String getComment() {
		return comment;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accountId == null) ? 0 : accountId.hashCode());
		result = prime * result + ((balance == null) ? 0 : balance.hashCode());
		result = prime * result + ((comment == null) ? 0 : comment.hashCode());
		result = prime * result + ((credit == null) ? 0 : credit.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((debit == null) ? 0 : debit.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		AccountBalance other = (AccountBalance) obj;
		if (accountId == null) {
			if (other.accountId != null)
				return false;
		} else if (!accountId.equals(other.accountId))
			return false;
		if (balance == null) {
			if (other.balance != null)
				return false;
		} else if (!balance.equals(other.balance))
			return false;
		if (comment == null) {
			if (other.comment != null)
				return false;
		} else if (!comment.equals(other.comment))
			return false;
		if (credit == null) {
			if (other.credit != null)
				return false;
		} else if (!credit.equals(other.credit))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (debit == null) {
			if (other.debit != null)
				return false;
		} else if (!debit.equals(other.debit))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
