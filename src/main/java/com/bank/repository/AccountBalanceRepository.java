package com.bank.repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bank.model.dao.AccountBalance;
/**
 * @author Ketan.Soneji
 */
@Repository
public interface AccountBalanceRepository extends JpaRepository<AccountBalance, Long> {

	public Optional<AccountBalance> findTopByAccountIdOrderByIdDesc (Long accountId);
	
	public List<AccountBalance> findByAccountIdAndDateBetweenOrderByIdDesc(Long accountId, Timestamp fromDate, Timestamp toDate);
}