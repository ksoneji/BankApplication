package com.bank.service;

import java.lang.invoke.MethodHandles;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.bank.auth.SecurityConstants;
import com.bank.model.dao.Employee;
import com.bank.repository.EmployeeRepository;
/**
 * <p>Service class to handle Employee related transactions.</p>
 * 
 * @author Ketan.Soneji
 */
@Service
public class EmployeeService implements UserDetailsService {
	private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
	
	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	/**
	 * <p>Save the emplyee details</p>
	 * 
	 * @param employee
	 * @return
	 */
	public Employee save(Employee employee) {

		// Avoid printing employee details to logs as it may contain confidential information
		logger.debug("Saving the employee details.");
		
		// Encode the password
		employee.setPassword(bCryptPasswordEncoder.encode(employee.getPassword()));

		// New Employee is not admin by default. The super admin or other admin can
		// change the setting
		employee.setIsAdmin(false);

		return employeeRepository.save(employee);
	}

	/**
	 * <p>Get the employee details by id</p>
	 * 
	 * @param id
	 * @return
	 */
	public Optional<Employee> getById(Long id) {
		logger.debug("Get the details for the employee with id::{}", id);
		return employeeRepository.findById(id);
	}

	/**
	 * <p>Get all employee details.</p>
	 * @return
	 */
	public List<Employee> getAll() {
		logger.debug("Get all employee details.");
		return employeeRepository.findAll();
	}

	/**
	 * <p>Deletes an employee</p>
	 * 
	 * @param id
	 */
	public void delete(Long id) {
		logger.debug("Delete the employee with id::{}", id);
		employeeRepository.deleteById(id);
	}

	/**
	 * Overridden method for authentication/authorization
	 */
	@Override
	public UserDetails loadUserByUsername(String username) {
		logger.debug("Loading user details for user::{}", username);
		Employee user = employeeRepository.findByUsername(username);

		if (user == null) {
			logger.debug("User::{} was not found.", username);
			throw new UsernameNotFoundException(username);
		}
		return new User(user.getUsername(), user.getPassword(), getAuthority(user));
	}

	/**
	 * <p>Set up the roles for authorization</p>
	 * 
	 * @param user
	 * @return
	 */
	private Set<SimpleGrantedAuthority> getAuthority(Employee user) {
		logger.debug("Setup the roles for the user::{}", user.getUsername());
		
		Set<SimpleGrantedAuthority> authorities = new HashSet<SimpleGrantedAuthority>();
		// Future: This can be expanded by having associating user with multiple roles.
		// For current demo, user can be Admin or non-admin
		if (user.getIsAdmin())
			authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		else
			authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		return authorities;
	}
	
	/**
	 * <p>Add the token to a blacklist after the logout and add it to a cache</p>
	 * 
	 * @param token
	 * @return
	 */
	@CachePut(value = SecurityConstants.BLACKLIST_TOKEN_CACHE, key="#token")
	public String logout (String token)
	{
		logger.debug("Updating cache with token::"+token);
		return token;
	}
}