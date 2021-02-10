package com.alliex.cvs.service;

import com.alliex.cvs.entity.TransactionDetail;
import com.alliex.cvs.repository.TransactionDetailRepository;
import com.alliex.cvs.exception.TransactionNotFoundException;
import com.alliex.cvs.web.dto.TransactionDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionDetailService {

    private final TransactionDetailRepository transactionDetailRepository;

    @Transactional
    public Long save(TransactionDetail transactionDetail) {
        return transactionDetailRepository.save(transactionDetail).getId();
    }

    @Transactional(readOnly = true)
    public List<TransactionDetailResponse> getDetailByRequestId(String requestId) {
        List<TransactionDetailResponse> transactionDetailResponses = transactionDetailRepository.findByRequestId(requestId);
        if (transactionDetailResponses.isEmpty()) {
            throw new TransactionNotFoundException(requestId);
        }

        return transactionDetailResponses;
    }

}
