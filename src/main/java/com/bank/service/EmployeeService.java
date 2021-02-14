package com.bank.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.bank.model.dao.Employee;
import com.bank.repository.EmployeeRepository;

@Service
public class EmployeeService implements UserDetailsService {

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	public Employee save(Employee employee) {

		// Encode the password
		employee.setPassword(bCryptPasswordEncoder.encode(employee.getPassword()));

		// New Employee is not admin by default. The super admin or other admin can
		// change the setting
		employee.setIsAdmin(false);

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
		return new User(user.getUsername(), user.getPassword(), getAuthority(user));
	}

	private Set<SimpleGrantedAuthority> getAuthority(Employee user) {
		Set<SimpleGrantedAuthority> authorities = new HashSet<SimpleGrantedAuthority>();
		// Future: This can be expanded by having associating user with multiple roles.
		// For current demo, user can be Admin or non-admin
		if (user.getIsAdmin())
			authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		else
			authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		return authorities;
	}

}