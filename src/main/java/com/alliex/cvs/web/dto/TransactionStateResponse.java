package com.alliex.cvs.web.dto;

import com.alliex.cvs.domain.type.TransactionState;
import com.alliex.cvs.entity.Transaction;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class TransactionStateResponse {

    private String requestId;

    private TransactionState transactionState;

    private LocalDateTime createdAt;

    public TransactionStateResponse(TransactionState transactionState, String requestId) {
        this.requestId = requestId;
        this.transactionState = transactionState;
    }

    public TransactionStateResponse(Transaction entity) {
        this.requestId = entity.getRequestId();
        this.transactionState = entity.getState();
        this.createdAt = entity.getCreatedAt();
    }

}
