package com.bank.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bank.model.dao.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

	@Query(value="SELECT * FROM accounts a WHERE a.status=1 AND a.interest_rate IS NOT NULL "
			+ "AND ((a.interest_calculated_date IS NOT NULL AND TIMESTAMPDIFF(YEAR, a.interest_calculated_date, UTC_DATE()) >= 1) "
			+ "OR (a.interest_calculated_date IS NULL AND TIMESTAMPDIFF(YEAR, a.created_date, UTC_DATE()) >= 1)) ", nativeQuery = true)
	public List<Account> findAccountsForInterestCalculation();
	
}