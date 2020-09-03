package com.alliex.cvs.domain.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;


public interface TransactionRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {

    Transaction findByTransNumber(String transNumber);

    List<Transaction> findByOriginId(Long transId);

}
