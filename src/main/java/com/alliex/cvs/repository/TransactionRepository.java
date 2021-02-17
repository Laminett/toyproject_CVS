package com.alliex.cvs.repository;

import com.alliex.cvs.entity.Transaction;
import com.alliex.cvs.web.dto.SettleTransMonthlySumRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {

    Optional<Transaction> findByRequestId(String requestId);

    Optional<Transaction> findByOriginRequestId(String requestId);

    @Query(value = "select t from Transaction t join fetch t.user u join fetch u.point p", countQuery = "select count(t) from Transaction t")
    Page<Transaction> findAllWithUser(Specification<Transaction> searchWith, Pageable pageable);

}