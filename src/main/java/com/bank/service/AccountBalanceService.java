package com.bank.service;

import java.lang.invoke.MethodHandles;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank.model.dao.AccountBalance;
import com.bank.model.json.TransferBalance;
import com.bank.repository.AccountBalanceRepository;

/**
 * <p>Service class to handle Account balance related transactions.</p>
 * 
 * @author Ketan.Soneji
 */
@Service
public class AccountBalanceService {

	private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
	
	@Autowired
	private AccountBalanceRepository acctBalanceRepository;

	@Autowired
	AccountBalanceService accBalanceService;
	
	/**
	 * <p>Save the account transaction.</p>
	 * 
	 * @param entity
	 * @return
	 */
	public AccountBalance save(AccountBalance entity) {
		
		logger.debug("Generating account transaction: {}", entity);
		
		/**
		 * There is no need to have an additional check if the account is debited/credited for the logged-in user
		 * since it is already authorized and it is assumed that the client (customer) has only those account ids
		 * that belongs to them. Account ids are unique to each customer. However, we can have an additional 
		 * check if required to once again verify by enforcing to pass the customer id in the payload and 
		 * re-verify it with logged in userid.
		 */
		double balance = 0.0;
		
		// Get last (previous) account balance for that account
		Optional<AccountBalance> accountBalance = accBalanceService.getLatestById(entity.getAccountId());
		
		if (accountBalance == null || accountBalance.isEmpty()) {
			logger.debug("Account Balance for id " + entity.getAccountId() + " does not exists");
		}
		else
		{
			balance = accountBalance.get().getBalance();
		}
		
		// Calculate the balance based on credit/debit type of transaction.
		balance = Objects.isNull(entity.getCredit())? balance: (balance + entity.getCredit());
		balance = Objects.isNull(entity.getDebit()) ? balance: (balance - entity.getDebit());
		
		// Set the account balance
		entity.setBalance(balance);
		return acctBalanceRepository.save(entity);
	}

	/**
	 * <p>Transfer the balance(credit/debit) between two accounts.</p>
	 * @param accounts
	 */
	public void transferBalance(TransferBalance accounts) {
		logger.debug("Transferring balance from account::{} to account:{}", accounts.getFromAccountId(), accounts.getToAccountId());
		
		/**
		 * Future: we can put an additional check to verify if the accounts belong the the logged in user.
		 * We can also put additional checks that the accounts exist. For now, its assumed that the client will
		 * make the rest call only if both the accounts are valid.
		 */
		
		/**
		 * Alternative: We can query the last balance for the source and target account instead of expecting it from the payload
		 */
		
		// Create a new account balance entry to debit the amount from the source account
		AccountBalance fromAccount = new AccountBalance();
		fromAccount.setAccountId(accounts.getFromAccountId());
		fromAccount.setDebit(accounts.getTransferAmount());
		fromAccount.setBalance(accounts.getFromAccountBalance() - accounts.getTransferAmount());
		
		// Create a new account balance entry to credit the amount to the target account
		AccountBalance toAccount = new AccountBalance();
		toAccount.setAccountId(accounts.getToAccountId());
		toAccount.setCredit(accounts.getTransferAmount());
		toAccount.setBalance(accounts.getToAccountBalance() + accounts.getTransferAmount());
		
		save(fromAccount);
		save(toAccount);
	}
	
	/**
	 * <p>Find account transaction by Id</p>
	 * 
	 * @param id
	 * @return
	 */
	public Optional<AccountBalance> getById(Long id) {
		logger.debug("Fetching details for the transaction id::{} ", id);
		return acctBalanceRepository.findById(id);
	}

	/**
	 * <p> Get all the account transactions. </p>
	 * 
	 * @return
	 */
	public List<AccountBalance> getAll() {
		logger.debug("Fetching all the transactions");
		return acctBalanceRepository.findAll();
	}

	/**
	 * <p>Delete an account transaction</p>
	 * 
	 * @param id
	 */
	public void delete(Long id) {
		logger.debug("Delete record for the transaction id::{} ", id);
		acctBalanceRepository.deleteById(id);
	}
	
	/**
	 * <p> Get the last transaction for an account.</p>
	 * 
	 * @param id
	 * @return
	 */
	public Optional<AccountBalance> getLatestById(Long id) {
		logger.debug("Fetching the latest transaction record for the account::{} ", id);
		return acctBalanceRepository.findTopByAccountIdOrderByIdDesc(id);
	}

	/**
	 * <p>Fetch transactions for an account between a specified date range </p>
	 * 
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	public List<AccountBalance> getByDateRange(Long accountId, String fromDate, String toDate) {
		logger.debug("Fetching account transactions for account::{} from date::{} to date::{}");
		return acctBalanceRepository.findByAccountIdAndDateBetweenOrderByIdDesc (accountId, Timestamp.valueOf(fromDate), Timestamp.valueOf(toDate));
	}

}