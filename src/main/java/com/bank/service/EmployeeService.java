package com.bank.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank.model.dao.Employee;
import com.bank.repository.EmployeeRepository;

@Service
public class EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;

	public Employee save(Employee entity) {
		return employeeRepository.save(entity);
	}

	public Optional<Employee> getById(Long id) {
		return employeeRepository.findById(id);
	}

	public List<Employee> getAll() {
		return employeeRepository.findAll();
	}

	public void delete(Long id) {
		employeeRepository.deleteById(id);
	}

}