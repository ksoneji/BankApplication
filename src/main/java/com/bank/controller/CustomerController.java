package com.bank.controller;

import java.lang.invoke.MethodHandles;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bank.model.dao.Customer;
import com.bank.model.dao.CustomerKyc;
import com.bank.service.CustomerService;

@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {

	 private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	@Autowired
	CustomerService custService;

	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Customer> addCustomer(@RequestBody Customer customer) {
		custService.save(customer);
		logger.debug("Added:: " + customer);
		return new ResponseEntity<Customer>(customer, HttpStatus.CREATED);
	}

	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	@RequestMapping(method = RequestMethod.PUT)
	public ResponseEntity<Void> updateCustomer(@RequestBody Customer customer) {
		Optional<Customer> existingCust = custService.getById(customer.getId());
		if (existingCust == null || existingCust.isEmpty()) {
			logger.debug("Customer with id " + customer.getId() + " does not exists");
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		} else {
			custService.save(customer);
			return new ResponseEntity<Void>(HttpStatus.OK);
		}
	}

	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Customer> getCustomer(@PathVariable("id") Long id) {
		Optional<Customer> customer = custService.getById(id);
		if (customer == null || customer.isEmpty()) {
			logger.debug("Customer with id " + id + " does not exists");
			return new ResponseEntity<Customer>(HttpStatus.NOT_FOUND);
		}
		logger.debug("Found Customer:: " + customer);
		return new ResponseEntity<Customer>(customer.get(), HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<Customer>> getAllCustomers() {
		List<Customer> customers = custService.getAll();
		if (customers.isEmpty()) {
			logger.debug("Customers does not exists");
			return new ResponseEntity<List<Customer>>(HttpStatus.NO_CONTENT);
		}
		logger.debug("Found " + customers.size() + " Customers");
		logger.debug(customers);
		logger.debug(Arrays.toString(customers.toArray()));
		return new ResponseEntity<List<Customer>>(customers, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> deleteCustomer(@PathVariable("id") Long id) {
		Optional<Customer> customer = custService.getById(id);
		if (customer == null || customer.isEmpty()) {
			logger.debug("Customer with id " + id + " does not exists");
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		} else {
			custService.delete(id);
			logger.debug("Customer with id " + id + " deleted");
			return new ResponseEntity<Void>(HttpStatus.GONE);
		}
	}

	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	@RequestMapping(value = "/kyc", method = RequestMethod.POST)
	public ResponseEntity<CustomerKyc> addCustomerKyc(@RequestBody CustomerKyc customerKyc) {
		
		Optional<Customer> existingCust = custService.getById(customerKyc.getCustomerId());
		if (existingCust == null || existingCust.isEmpty()) {
			logger.debug("Customer with id " + customerKyc.getCustomerId() + " does not exists");
			return new ResponseEntity<CustomerKyc>(HttpStatus.NOT_FOUND);
		} else {
			// Additional check. Ideally the UI should not send this call in case of updates. It should use the PUT method for updates
			// Alternatively, we can merge the POST/PUT into one call and handle creates/updates.
			Optional<CustomerKyc> existingCustKyc = custService.getCustomerKyc(customerKyc.getCustomerId());
			if (existingCustKyc.isPresent()) {
				logger.debug("KYC for Customer with id " + customerKyc.getId() + " does not exists");
				return new ResponseEntity<CustomerKyc>(HttpStatus.BAD_REQUEST);
			}		
			logger.debug("Added KYC:: " + customerKyc);
			custService.saveCustomerKyc(customerKyc);
			return new ResponseEntity<CustomerKyc>(customerKyc, HttpStatus.CREATED);
		}
	}

	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	@RequestMapping(value = "/kyc", method = RequestMethod.PUT)
	public ResponseEntity<Void> updateCustomerKyc(@RequestBody CustomerKyc customerKyc) {
		Optional<CustomerKyc> existingCustKyc = custService.getCustomerKyc(customerKyc.getCustomerId());
		if (existingCustKyc == null || existingCustKyc.isEmpty()) {
			logger.debug("KYC for Customer with id " + customerKyc.getId() + " does not exists");
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		} else {
			logger.debug("Updated KYC:: " + customerKyc);
			Timestamp currentTimeStamp = new Timestamp(System.currentTimeMillis());
			customerKyc.setModifiedDate(currentTimeStamp);
			custService.saveCustomerKyc(customerKyc);
			return new ResponseEntity<Void>(HttpStatus.OK);
		}
	}
	
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	@RequestMapping(value = "/kyc/{customerId}", method = RequestMethod.GET)
	public ResponseEntity<CustomerKyc> getCustomerKyc(@PathVariable("customerId") Long customerId) {
		Optional<CustomerKyc> customerKyc = custService.getCustomerKyc(customerId);
		if (customerKyc == null || customerKyc.isEmpty()) {
			logger.debug("KYC for Customer with id " + customerId + " does not exists");
			return new ResponseEntity<CustomerKyc>(HttpStatus.NOT_FOUND);
		}
		logger.debug("Found Customer Kyc:: " + customerKyc);
		return new ResponseEntity<CustomerKyc>(customerKyc.get(), HttpStatus.OK);
	}
}