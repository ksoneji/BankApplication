package com.bank.controller;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bank.model.dao.Account;
import com.bank.model.dao.AccountBalance;
import com.bank.model.json.TransferBalance;
import com.bank.service.AccountBalanceService;
import com.bank.service.AccountService;
import com.bank.utils.AccountPDFExporter;
import com.lowagie.text.DocumentException;

@RestController
@RequestMapping("/api/v1/account")
public class AccountController {

	private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	@Autowired
	AccountService accService;

	@Autowired
	AccountBalanceService accBalanceService;

	@Value("${pdf.export.path}")
	private String pdfExportPath;

	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Account> addAccount(@RequestBody Account account) {
		accService.save(account);
		logger.debug("Added:: " + account);
		return new ResponseEntity<Account>(account, HttpStatus.CREATED);
	}

	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	@RequestMapping(value = "/balance", method = RequestMethod.POST)
	public ResponseEntity<AccountBalance> addAccountBalance(@RequestBody AccountBalance account) {
		accBalanceService.save(account);
		logger.debug("Added:: " + account);
		return new ResponseEntity<AccountBalance>(account, HttpStatus.CREATED);
	}

	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	@RequestMapping(value = "/transfer", method = RequestMethod.POST)
	public ResponseEntity<Void> transferBalance(@RequestBody TransferBalance accounts) {
		/**
		 * Assumption1: The client already has the to & from accounts and the payload
		 * would always have both the account ids If any of the account is not present
		 * then the client should not make this rest call. Assumption2: The client has
		 * already verified that the account has enough balance to be transferred to
		 * other account.
		 */
		accBalanceService.transferBalance(accounts);
		logger.debug("transferred balance from:: " + accounts.getFromAccountId() + " to::" + accounts.getToAccountId());
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
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

	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
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

	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	@RequestMapping(value = "/report/{id}", method = RequestMethod.GET)
	public ResponseEntity<byte[]> getAccountReportByRange(@PathVariable("id") Long id,
			@RequestParam(required = true) String fromDate, @RequestParam(required = true) String toDate)
			throws DocumentException, IOException {
		
		Optional<Account> account = accService.getById(id);
		if (account == null || account.isEmpty()) {
			logger.debug("Account with id " + id + " does not exists");
			return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
		}
		logger.debug("Found Account:: " + account);

		List<AccountBalance> accountBalances = accBalanceService.getByDateRange(fromDate, toDate);
		if (accountBalances == null || accountBalances.isEmpty()) {
			logger.debug("Account Balance for id " + id + " does not exists");
			return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
		}

		logger.debug("Found AccountBalances:: " + accountBalances);

		String filename = "account_" + System.currentTimeMillis() + ".pdf";

		AccountPDFExporter exporter = new AccountPDFExporter(accountBalances);
		byte[] contents = exporter.export(pdfExportPath + filename, String.valueOf(id), fromDate, toDate);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PDF);

		headers.setContentDispositionFormData(filename, filename);
		headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

		return new ResponseEntity<byte[]>(contents, headers, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
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