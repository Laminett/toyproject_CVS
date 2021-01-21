package com.alliex.cvs.service;

import com.alliex.cvs.domain.type.TransactionState;
import com.alliex.cvs.domain.transaction.Transaction;
import com.alliex.cvs.domain.transaction.TransactionRepository;
import com.alliex.cvs.domain.transactionDetail.TransactionDetail;
import com.alliex.cvs.domain.transactionDetail.TransactionDetailRepository;
import com.alliex.cvs.exception.TransactionDetailNotFoundException;
import com.alliex.cvs.exception.TransactionNotFoundException;
import com.alliex.cvs.web.dto.TransactionDetailResponse;
import com.alliex.cvs.web.dto.TransactionDetailSaveRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionDetailService {

    private final TransactionDetailRepository transactionDetailRepository;

    @Transactional
    public Long save(TransactionDetailSaveRequest transactionDetailSaveRequest) {
        return transactionDetailRepository.save(transactionDetailSaveRequest.toEntity()).getId();
    }

    @Transactional
    public String update(String requestId, TransactionState transactionState) {
        List<TransactionDetailResponse> transactionDetailResponses = transactionDetailRepository.findByRequestId(requestId);
        for (TransactionDetailResponse transDetail : transactionDetailResponses) {
            TransactionDetail transactionDetail = transactionDetailRepository.findById(transDetail.getId()).orElseThrow(()
                    -> new TransactionDetailNotFoundException(transDetail.getId()));
            transactionDetail.update(transactionState);
        }

        return requestId;
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
