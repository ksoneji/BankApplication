package com.bank.service;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank.model.dao.Customer;
import com.bank.model.dao.CustomerKyc;
import com.bank.repository.CustomerKycRepository;
import com.bank.repository.CustomerRepository;
/**
 * <p>Service class to handle Customer related transactions.</p>
 * 
 * @author Ketan.Soneji
 */
@Service
public class CustomerService {

	private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
	
	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private CustomerKycRepository customerKycRepository;
	
	/**
	 * <p>Save the customer details.</p>
	 * 
	 * @param entity
	 * @return
	 */
	public Customer save(Customer entity) {
		logger.debug("Saving customer info::{}", entity);
		return customerRepository.save(entity);
	}

	/**
	 * <p>Get customer by ID</p>
	 * 
	 * @param id
	 * @return
	 */
	public Optional<Customer> getById(Long id) {
		logger.debug("Fetch customer by id::{}", id);
		return customerRepository.findById(id);
	}

	/**
	 * <p>Get all customers</p>
	 * 
	 * @return
	 */
	public List<Customer> getAll() {
		logger.debug("Get all customers");
		return customerRepository.findAll();
	}

	/**
	 * <p>Delete customer by id</p>
	 * 
	 * @param id
	 */
	public void delete(Long id) {
		logger.debug("Delete customer by id::{}", id);
		customerRepository.deleteById(id);
	}
	
	/**
	 * <p>Save the KYC details for a customer</p>
	 * 
	 * @param entity
	 * @return
	 */
	public CustomerKyc saveCustomerKyc(CustomerKyc entity) {
		logger.debug("Save customer kyc for customer::{}", entity.getCustomerId());
		return customerKycRepository.save(entity);
	}

	/**
	 * <p>Get the KYC info for a customer</p>
	 * 
	 * @param customerId
	 * @return
	 */
	public Optional<CustomerKyc> getCustomerKyc(Long customerId) {
		logger.debug("Get customer kyc info for customer::{}", customerId);
		return customerKycRepository.findByCustomerId(customerId);
	}

}