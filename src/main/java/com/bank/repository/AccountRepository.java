package com.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bank.model.dao.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

}