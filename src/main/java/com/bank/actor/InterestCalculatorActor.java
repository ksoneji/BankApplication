package com.bank.actor;

import java.lang.invoke.MethodHandles;
import java.sql.Timestamp;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bank.model.dao.Account;
import com.bank.model.dao.AccountBalance;
import com.bank.service.AccountBalanceService;
import com.bank.service.AccountService;

import akka.actor.UntypedAbstractActor;

/**
 * <p>
 * The Actor to help execute APIs to calculate interest.
 * </p>
 * 
 * @author Ketan Soneji
 */
@Component("InterestCalculatorActor")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class InterestCalculatorActor extends UntypedAbstractActor {
	private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	@Autowired
	AccountBalanceService accBalanceService;

	@Autowired
	AccountService accountService;

	@Override
	public void preStart() throws Exception {
		logger.debug("Initiating InterestCalculatorActor with hashCode::{}", this.hashCode());
		super.preStart();
	}

	@Override
	public void onReceive(Object message) throws Throwable {
		if (message instanceof Account) {
			logger.debug("Actor with hashCode::{} executing interest calculation for account id::{}", this.hashCode(),
					((Account) message).getId());

			Account account = (Account) message;
			Timestamp currentTimeStamp = new Timestamp(System.currentTimeMillis());

			// Get latest balance
			Optional<AccountBalance> latestAccountBalance = accBalanceService.getLatestById(account.getId());

			// Calculate interest only if account balance is present
			if (latestAccountBalance.isPresent()) {
				// Calculate simple interest: Yearly interest, Interest rate is defaulted to
				// 3.5 in the data model and can be overwritten in the payload
				double interest = (latestAccountBalance.get().getBalance() * account.getInterestRate() * 1) / 100;

				AccountBalance balance = new AccountBalance();
				balance.setAccountId(account.getId());
				balance.setCredit(interest);
				balance.setComment("Annual Interest Credited");

				// Credit the interest and save the new account balance record
				accBalanceService.save(balance);

				// Update the interest calculated date in the account record
				account.setInterestCalculatedDate(currentTimeStamp);
				accountService.save(account);
			}
		} else {
			unhandled(message);
		}
	}
}