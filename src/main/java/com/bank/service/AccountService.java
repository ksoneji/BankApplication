package com.bank.service;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank.model.dao.Account;
import com.bank.repository.AccountRepository;
/**
 * <p>Service class to handle Account related transactions.</p>
 * 
 * @author Ketan.Soneji
 */
@Service
public class AccountService {

	private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
	
	@Autowired
	private AccountRepository accountRepository;

	/**
	 * <p>Save the account information</p>
	 * 
	 * @param entity
	 * @return
	 */
	public Account save(Account entity) {
		logger.debug("Saving the account::{}", entity);
		return accountRepository.save(entity);
	}

	/**
	 * <p>Get the account information by Id</p>
	 * 
	 * @param id
	 * @return
	 */
	public Optional<Account> getById(Long id) {
		logger.debug("Fetchomg the account with id::{}", id);
		return accountRepository.findById(id);
	}

	/**
	 * <p>Get all account details.</p>
	 * 
	 * @return
	 */
	public List<Account> getAll() {
		logger.debug("Get all account details");
		return accountRepository.findAll();
	}

	/**
	 * <p>Delete an account by id</p>
	 * 
	 * @param id
	 */
	public void delete(Long id) {
		logger.debug("Deleting the account with id::{}", id);
		accountRepository.deleteById(id);
	}
	
	/**
	 * <p>Get all active accounts for which interest calculation is due.</p>
	 * 
	 * @return
	 */
	public List<Account> getAccountsForInterestCalculation() {
		logger.debug("Get all active accounts for which interest calculation is due.");
		return accountRepository.findAccountsForInterestCalculation();
	}
	

}