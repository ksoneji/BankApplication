package com.bank.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank.model.dao.Account;
import com.bank.repository.AccountRepository;

@Service
public class AccountService {

	@Autowired
	private AccountRepository accountRepository;

	public Account save(Account entity) {
		return accountRepository.save(entity);
	}

	public Optional<Account> getById(Long id) {
		return accountRepository.findById(id);
	}

	public List<Account> getAll() {
		return accountRepository.findAll();
	}

	public void delete(Long id) {
		accountRepository.deleteById(id);
	}
	
	public List<Account> getAccountsForInterestCalculation() {
		return accountRepository.findAccountsForInterestCalculation();
	}
	

}