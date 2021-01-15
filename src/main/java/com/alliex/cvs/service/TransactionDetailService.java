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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


@Service
public class TransactionDetailService {

    private final TransactionDetailRepository transactionDetailRepository;

    private final TransactionRepository transactionRepository;

    public TransactionDetailService(TransactionDetailRepository transactionDetailRepository, TransactionRepository transactionRepository) {
        this.transactionDetailRepository = transactionDetailRepository;
        this.transactionRepository = transactionRepository;
    }

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
    public List<TransactionDetailResponse> getDetails(String requestId) {
        transactionRepository.findByRequestId(requestId).orElseThrow(()
                -> new TransactionNotFoundException(requestId));

        return transactionDetailRepository.findByRequestId(requestId);
    }

}
