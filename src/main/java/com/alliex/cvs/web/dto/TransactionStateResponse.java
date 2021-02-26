package com.alliex.cvs.web.dto;

import com.alliex.cvs.entity.Transaction;
import com.alliex.cvs.domain.type.TransactionState;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class TransactionStateResponse {

    private TransactionState transactionState;

    private String requestId;

    private LocalDateTime createdDate;

    public TransactionStateResponse(TransactionState transactionState, String requestId) {
        this.requestId = requestId;
        this.transactionState = transactionState;
    }

    public TransactionStateResponse(Transaction entity) {
        this.requestId = entity.getRequestId();
        this.transactionState = entity.getState();
        this.createdDate = entity.getCreatedDate();
    }

}
