package com.alliex.cvs.web.dto;

import com.alliex.cvs.domain.transaction.TransState;
import com.alliex.cvs.domain.transaction.TransType;
import com.alliex.cvs.domain.transaction.Transaction;
import com.alliex.cvs.domain.user.User;
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

    public TransactionResponse(Long id, TransState transState, Long buyerId, Long merchantId, Integer transPoint, TransType transType, String paymentType,
                               LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.transState = transState;
        this.buyerId = buyerId;
        this.merchantId = merchantId;
        this.transPoint = transPoint;
        this.transType = transType;
        this.paymentType = paymentType;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

}
