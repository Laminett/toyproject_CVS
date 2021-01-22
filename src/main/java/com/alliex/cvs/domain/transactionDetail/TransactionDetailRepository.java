package com.alliex.cvs.domain.transactionDetail;

import com.alliex.cvs.web.dto.TransactionDetailResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface TransactionDetailRepository extends JpaRepository<TransactionDetail, Long> {

    List<TransactionDetailResponse> findByRequestId(String requestId);

}
