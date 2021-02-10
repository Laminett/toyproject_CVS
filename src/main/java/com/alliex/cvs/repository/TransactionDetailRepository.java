package com.alliex.cvs.repository;

import com.alliex.cvs.entity.TransactionDetail;
import com.alliex.cvs.web.dto.TransactionDetailResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface TransactionDetailRepository extends JpaRepository<TransactionDetail, Long> {

    List<TransactionDetailResponse> findByRequestId(String requestId);

}
