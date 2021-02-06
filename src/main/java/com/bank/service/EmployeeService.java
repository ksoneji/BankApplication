package com.bank.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.bank.model.dao.Employee;
import com.bank.repository.EmployeeRepository;

@Service
public class EmployeeService implements UserDetailsService{

	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	public Employee save(Employee employee) {
		
		// Encode the password
		employee.setPassword(bCryptPasswordEncoder.encode(employee.getPassword()));
		
		return employeeRepository.save(employee);
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
	
    @Override
    public UserDetails loadUserByUsername(String username) {
        Employee user = employeeRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new User(user.getUsername(), user.getPassword(), new ArrayList<>());
    }	

}