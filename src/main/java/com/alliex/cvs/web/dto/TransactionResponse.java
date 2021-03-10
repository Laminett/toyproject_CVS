package com.alliex.cvs.web.dto;

import com.alliex.cvs.entity.Transaction;
import com.alliex.cvs.domain.type.PaymentType;
import com.alliex.cvs.domain.type.TransactionState;
import com.alliex.cvs.domain.type.TransactionType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TransactionResponse {

    private Long id;

    private TransactionState state;

    private String username;

    private String originRequestId;

    private Long point;

    private TransactionType type;

    private String requestId;

    private PaymentType paymentType;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    private List<TransactionDetailResponse> transactionItems;

    private Boolean isRefunded;

    public TransactionResponse(Transaction entity) {
        this.id = entity.getId();
        this.state = entity.getState();
        this.username = entity.getUser().getUsername();
        this.originRequestId = entity.getOriginRequestId();
        this.point = entity.getPoint();
        this.type = entity.getType();
        this.requestId = entity.getRequestId();
        this.paymentType = entity.getPaymentType();
        this.createdDate = entity.getCreatedDate();
        this.modifiedDate = entity.getModifiedDate();
    }

}
