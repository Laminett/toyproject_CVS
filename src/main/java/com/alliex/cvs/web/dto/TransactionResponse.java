package com.alliex.cvs.web.dto;

import com.alliex.cvs.domain.transaction.Transaction;
import com.alliex.cvs.domain.type.TransactionState;
import com.alliex.cvs.domain.type.TransactionType;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TransactionResponse {

    private TransactionState transactionState;

    private String requestId;

    public TransactionResponse(TransactionState transactionState, String requestId) {
        this.requestId = requestId;
        this.transactionState = transactionState;
    }

    public TransactionResponse(Transaction entity) {
        this.requestId = entity.getRequestId();
        this.transactionState = entity.getState();

    }
}
