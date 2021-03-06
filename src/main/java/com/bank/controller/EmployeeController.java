package com.bank.controller;

import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

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

import com.bank.auth.SecurityConstants;
import com.bank.model.dao.Employee;
import com.bank.service.EmployeeService;
/**
 * <p>Rest Controller to handle Employee related transactions</p>
 * 
 * @author Ketan.Soneji
 */
@RestController
@RequestMapping("/api/v1/employee")
public class EmployeeController {
	private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	@Autowired
	EmployeeService empService;

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResponseEntity<Employee> signUp(@RequestBody Employee employee) {
		empService.save(employee);
		logger.debug("Added:: " + employee);
		return new ResponseEntity<Employee>(employee, HttpStatus.CREATED);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(method = RequestMethod.PUT)
	public ResponseEntity<Void> updateEmployee(@RequestBody Employee employee) {
		Optional<Employee> existingEmp = empService.getById(employee.getId());
		if (existingEmp == null || existingEmp.isEmpty()) {
			logger.debug("Employee with id " + employee.getId() + " does not exists");
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		} else {
			empService.save(employee);
			return new ResponseEntity<Void>(HttpStatus.OK);
		}
	}

	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Employee> getEmployee(@PathVariable("id") Long id) {
		Optional<Employee> employee = empService.getById(id);
		if (employee == null || employee.isEmpty()) {
			logger.debug("Employee with id " + id + " does not exists");
			return new ResponseEntity<Employee>(HttpStatus.NOT_FOUND);
		}
		logger.debug("Found Employee:: " + employee);
		return new ResponseEntity<Employee>(employee.get(), HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<Employee>> getAllEmployees() {
		List<Employee> employees = empService.getAll();
		if (employees.isEmpty()) {
			logger.debug("Employees does not exists");
			return new ResponseEntity<List<Employee>>(HttpStatus.NO_CONTENT);
		}
		logger.debug("Found " + employees.size() + " Employees");
		logger.debug(employees);
		logger.debug(Arrays.toString(employees.toArray()));
		return new ResponseEntity<List<Employee>>(employees, HttpStatus.OK);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> deleteEmployee(@PathVariable("id") Long id) {
		Optional<Employee> employee = empService.getById(id);
		if (employee == null || employee.isEmpty()) {
			logger.debug("Employee with id " + id + " does not exists");
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		} else {
			empService.delete(id);
			logger.debug("Employee with id " + id + " deleted");
			return new ResponseEntity<Void>(HttpStatus.GONE);
		}
	}

	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	public ResponseEntity<Void> logout(HttpServletRequest request) {
		String token = request.getHeader(SecurityConstants.HEADER_STRING);
		empService.logout(token);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

}