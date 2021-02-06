package com.bank.controller;

import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.List;
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

import com.bank.model.dao.Customer;
import com.bank.service.CustomerService;

@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {

	 private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	@Autowired
	CustomerService custService;

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Customer> addCustomer(@RequestBody Customer customer) {
		custService.save(customer);
		logger.debug("Added:: " + customer);
		return new ResponseEntity<Customer>(customer, HttpStatus.CREATED);
	}


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

}