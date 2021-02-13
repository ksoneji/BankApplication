package com.bank.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank.model.dao.Customer;
import com.bank.model.dao.CustomerKyc;
import com.bank.repository.CustomerKycRepository;
import com.bank.repository.CustomerRepository;

@Service
public class CustomerService {

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private CustomerKycRepository customerKycRepository;
	
	public Customer save(Customer entity) {
		return customerRepository.save(entity);
	}

	public Optional<Customer> getById(Long id) {
		return customerRepository.findById(id);
	}

	public List<Customer> getAll() {
		return customerRepository.findAll();
	}

	public void delete(Long id) {
		customerRepository.deleteById(id);
	}
	
	public CustomerKyc saveCustomerKyc(CustomerKyc entity) {
		return customerKycRepository.save(entity);
	}

	public Optional<CustomerKyc> getCustomerKyc(Long customerId) {
		return customerKycRepository.findByCustomerId(customerId);
	}

}