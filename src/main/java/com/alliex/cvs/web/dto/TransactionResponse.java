package com.alliex.cvs.web.dto;

import com.alliex.cvs.domain.transaction.TransState;
import com.alliex.cvs.domain.transaction.TransType;
import com.alliex.cvs.domain.transaction.Transaction;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TransactionResponse {

    private Long id;

    private TransState transState;

    private Long buyerId;

    private Long merchantId;

    private Integer transPoint;

    private TransType transType;

    private String paymentType;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    public TransactionResponse(Transaction entity) {
        this.id = entity.getId();
        this.transState = entity.getTransState();
        this.buyerId = entity.getBuyerId();
        this.merchantId = entity.getMerchantId();
        this.transPoint = entity.getTransPoint();
        this.transType = entity.getTransType();
        this.paymentType = entity.getPaymentType();
        this.createdDate = entity.getCreatedDate();
        this.modifiedDate = entity.getModifiedDate();
    }

}
