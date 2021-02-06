package com.bank.controller;

import java.lang.invoke.MethodHandles;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bank.model.dao.Account;
import com.bank.model.dao.AccountBalance;
import com.bank.service.AccountBalanceService;
import com.bank.service.AccountService;

@RestController
@RequestMapping("/api/v1/account")
public class AccountController {

	 private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	@Autowired
	AccountService accService;
	
	@Autowired
	AccountBalanceService accBalanceService;
	

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Account> addAccount(@RequestBody Account account) {
		accService.save(account);
		logger.debug("Added:: " + account);
		return new ResponseEntity<Account>(account, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/balance", method = RequestMethod.POST)
	public ResponseEntity<AccountBalance> addAccountBalance(@RequestBody AccountBalance account) {
		accBalanceService.save(account);
		logger.debug("Added:: " + account);
		return new ResponseEntity<AccountBalance>(account, HttpStatus.CREATED);
	}

	@RequestMapping(method = RequestMethod.PUT)
	public ResponseEntity<Void> updateAccount(@RequestBody Account account) {
		Optional<Account> existingAcct = accService.getById(account.getId());
		if (existingAcct == null || existingAcct.isEmpty()) {
			logger.debug("Account with id " + account.getId() + " does not exists");
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		} else {
			accService.save(account);
			return new ResponseEntity<Void>(HttpStatus.OK);
		}
	}


	@RequestMapping(value = "/{id}/balance", method = RequestMethod.GET)
	public ResponseEntity<AccountBalance> getAccount(@PathVariable("id") Long id) {
		Optional<Account> account = accService.getById(id);
		if (account == null || account.isEmpty()) {
			logger.debug("Account with id " + id + " does not exists");
			return new ResponseEntity<AccountBalance>(HttpStatus.NOT_FOUND);
		}
		logger.debug("Found Account:: " + account);
		
		Optional<AccountBalance> accountBalance = accBalanceService.getLatestById(id);
		if (accountBalance == null || accountBalance.isEmpty()) {
			logger.debug("Account Balance for id " + id + " does not exists");
			return new ResponseEntity<AccountBalance>(HttpStatus.NOT_FOUND);
		}
		logger.debug("Found AccountBalance:: " + accountBalance);
		return new ResponseEntity<AccountBalance>(accountBalance.get(), HttpStatus.OK);
	}


	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> deleteAccount(@PathVariable("id") Long id) {
		Optional<Account> employee = accService.getById(id);
		if (employee == null || employee.isEmpty()) {
			logger.debug("Account with id " + id + " does not exists");
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		} else {
			accService.delete(id);
			logger.debug("Account with id " + id + " deleted");
			return new ResponseEntity<Void>(HttpStatus.GONE);
		}
	}

}