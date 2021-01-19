package com.alliex.cvs.web.dto;

import com.alliex.cvs.domain.transaction.Transaction;
import com.alliex.cvs.domain.type.PaymentType;
import com.alliex.cvs.domain.type.TransactionState;
import com.alliex.cvs.domain.type.TransactionType;
import com.alliex.cvs.domain.user.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class TransactionResponse {

    private Long id;

    private TransactionState state;

    private Long userId;

    private Long merchantId;

    private Long originId;

    private Long point;

    private TransactionType type;

    private String requestId;

    private PaymentType paymentType;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifiedDate;

    public TransactionResponse(Transaction entity) {
        this.id = entity.getId();
        this.state = entity.getState();
        // User -> Transaction 관계 임시 해제를 위한 주석
//        this.user = entity.getUser();
        this.userId = entity.getUserId();
        this.originId = entity.getOriginId();
        this.point = entity.getPoint();
        this.type = entity.getType();
        this.requestId = entity.getRequestId();
        this.paymentType = entity.getPaymentType();
        this.createdDate = entity.getCreatedDate();
        this.modifiedDate = entity.getModifiedDate();
    }

}
