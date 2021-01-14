package com.alliex.cvs.domain.transaction;

import com.alliex.cvs.web.dto.SettleTransMonthlySumRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {

    Optional<Transaction> findByRequestId(String transNumber);

    List<Transaction> findByOriginId(Long transId);

    @Query(value = "select " +
            "new com.alliex.cvs.web.dto.SettleTransMonthlySumRequest(" +
            // User -> Transaction 관계 임시 해제를 위한 주석
//            "t.user.id" +
            "t.userId" +
            ", sum(case when (t.type = 'PAYMENT') then 1 else 0 end)" +
            ", sum(case when (t.type = 'PAYMENT') then t.point else 0 end)" +
            ", sum(case when (t.type = 'REFUND') then 1 else 0 end)" +
            ", sum(case when (t.type = 'REFUND') then t.point else 0 end))" +
            "from Transaction t " +
            "where t.createdDate >= :fromDate " +
            "and t.createdDate < :toDate " +
            // User -> Transaction 관계 임시 해제를 위한 주석
//            "group by t.user.id")
            "group by t.userId")
    List<SettleTransMonthlySumRequest> findByCreatedDate(@Param("fromDate") LocalDateTime fromDate, @Param("toDate") LocalDateTime toDate);

}