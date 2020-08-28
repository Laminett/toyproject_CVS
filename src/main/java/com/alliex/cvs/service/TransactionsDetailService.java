package com.alliex.cvs.service;

import com.alliex.cvs.domain.transaction.TransState;
import com.alliex.cvs.domain.transaction.Transaction;
import com.alliex.cvs.domain.transaction.TransactionRepository;
import com.alliex.cvs.domain.transactionDetail.TransactionDetail;
import com.alliex.cvs.domain.transactionDetail.TransactionDetailRepository;
import com.alliex.cvs.exception.TransactionNotFoundException;
import com.alliex.cvs.web.dto.TransactionDetailResponse;
import com.alliex.cvs.web.dto.TransactionDetailSaveRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class TransactionsDetailService {

    private final TransactionDetailRepository transactionDetailRepository;

    private final TransactionRepository transactionRepository;

    public TransactionsDetailService(TransactionDetailRepository transactionDetailRepository, TransactionRepository transactionRepository) {
        this.transactionDetailRepository = transactionDetailRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public Long save(TransactionDetailSaveRequest transactionDetailSaveRequest) {
        return transactionDetailRepository.save(transactionDetailSaveRequest.toEntity()).getId();
    }

    @Transactional
    public Long update(Long transid, TransState transState) {
        List<TransactionDetailResponse> transactionDetailResponses = transactionDetailRepository.findByTransId(transid);
        for (TransactionDetailResponse transDetail : transactionDetailResponses) {
            TransactionDetail transactionDetail = transactionDetailRepository.findById(transDetail.getId()).orElseThrow(()
                    -> new TransactionNotFoundException("Not found TransactionDetail id : " + transDetail.getId()));
            transactionDetail.update(transState);
        }

        return transid;
    }

    @Transactional(readOnly = true)
    public List<TransactionDetailResponse> getDetails(Long transId) {
        Transaction transaction = transactionRepository.findById(transId).orElseThrow(()
                -> new TransactionNotFoundException("Not Found Transaction Id : " + transId));
        if(transaction.getOriginId() == null){
            return transactionDetailRepository.findByTransId(transId);
        } else {
            return transactionDetailRepository.findByTransId(transaction.getOriginId());
        }
    }

}
