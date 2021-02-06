package com.bank.service;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bank.model.dao.AccountBalance;
import com.bank.repository.AccountBalanceRepository;

@Service
public class AccountBalanceService {

	private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
	
	@Autowired
	private AccountBalanceRepository acctBalanceRepository;

	@Autowired
	AccountBalanceService accBalanceService;
	
	public AccountBalance save(AccountBalance entity) {
		
		double balance = 0.0;
		
		// Get previous account balance
		Optional<AccountBalance> accountBalance = accBalanceService.getLatestById(entity.getAccountId());
		if (accountBalance == null || accountBalance.isEmpty()) {
			logger.debug("Account Balance for id " + entity.getAccountId() + " does not exists");
		}
		else
		{
			// Initialize the account balance
			balance = accountBalance.get().getBalance();
		}
		
		// Calculate the balance based on credit/debit type of transaction.
		balance = entity.getCredit() == 0.0 ? balance: (balance + entity.getCredit());
		balance = entity.getDebit() == 0.0 ? balance: (balance - entity.getDebit());
		
		entity.setBalance(balance);
		return acctBalanceRepository.save(entity);
	}

	public Optional<AccountBalance> getById(Long id) {
		return acctBalanceRepository.findById(id);
	}

	public List<AccountBalance> getAll() {
		return acctBalanceRepository.findAll();
	}

	public void delete(Long id) {
		acctBalanceRepository.deleteById(id);
	}
	
	public Optional<AccountBalance> getLatestById(Long id) {
		return acctBalanceRepository.findTopByAccountIdOrderByIdDesc(id);
	}

}