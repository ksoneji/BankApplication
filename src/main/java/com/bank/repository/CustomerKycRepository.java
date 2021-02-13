package com.bank.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bank.model.dao.CustomerKyc;

@Repository
public interface CustomerKycRepository extends JpaRepository<CustomerKyc, Long> {

	public Optional<CustomerKyc> findByCustomerId (Long customerId);
}