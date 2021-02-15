package com.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bank.model.dao.Employee;
/**
 * @author Ketan.Soneji
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	/**
	 * <p> Returns the user record based on the username</p>
	 * @param userId
	 * @return
	 */
	Employee findByUsername(String username);
}