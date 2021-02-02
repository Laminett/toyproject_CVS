package com.alliex.cvs.domain.transaction;

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

    @Query(value = "select " +
            "new com.alliex.cvs.web.dto.SettleTransMonthlySumRequest(" +
            "t.user.id" +
            ", sum(case when (t.type = 'PAYMENT') then 1 else 0 end)" +
            ", sum(case when (t.type = 'PAYMENT') then t.point else 0 end)" +
            ", sum(case when (t.type = 'REFUND') then 1 else 0 end)" +
            ", sum(case when (t.type = 'REFUND') then t.point else 0 end))" +
            "from Transaction t " +
            "where t.createdDate >= :fromDate " +
            "and t.createdDate < :toDate " +
            "group by t.user.id")
    List<SettleTransMonthlySumRequest> findByCreatedDate(@Param("fromDate") LocalDateTime fromDate, @Param("toDate") LocalDateTime toDate);

    @Query(value = "select t from Transaction t join fetch t.user u join fetch u.point p", countQuery = "select count(t) from Transaction t")
    Page<Transaction> findAllWithUser(Specification<Transaction> searchWith, Pageable pageable);

}