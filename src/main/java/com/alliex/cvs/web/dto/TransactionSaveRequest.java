package com.alliex.cvs.web.dto;

import com.alliex.cvs.domain.type.PaymentType;
import com.alliex.cvs.domain.type.TransactionState;
import com.alliex.cvs.domain.type.TransactionType;
import com.alliex.cvs.domain.transaction.Transaction;
import com.alliex.cvs.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class TransactionSaveRequest {

    private Long userId;

    private Long merchantId;

    private Long originId;

    private Long point;

    private PaymentType paymentType;

    private TransactionState state;

    private TransactionType type;

    private String requestId;

    private List<Map<String, String>> transProduct;

    @Builder
    public TransactionSaveRequest(Long userId, Long merchantId, Long originId, Long point, TransactionState transactionState, TransactionType transactionType, String requestId, List<Map<String, String>> transProduct) {
        this.userId = userId;
        this.merchantId = merchantId;
        this.originId = originId;
        this.point = point;
        this.state = transactionState;
        this.type = transactionType;
        this.requestId = requestId;
        this.transProduct = transProduct;
    }

    @Builder
    public TransactionSaveRequest(Long userId, Long merchantId, Long originId, Long point, TransactionState transactionState, TransactionType transactionType, String requestId, PaymentType paymentType) {
        this.userId = userId;
        this.merchantId = merchantId;
        this.originId = originId;
        this.point = point;
        this.state = transactionState;
        this.type = transactionType;
        this.requestId = requestId;
        this.paymentType = paymentType;
    }

    public Transaction toEntity() {
        // User -> Transaction 관계 임시 해제를 위한 주석
//        User setUserId = new User();
//        setUserId.setId(userId);

        return Transaction.builder()
                // User -> Transaction 관계 임시 해제를 위한 주석
//                .user(setUserId)
                .userId(userId)
                .merchantId(merchantId)
                .originId(originId)
                .point(point)
                .transactionState(state)
                .transactionType(type)
                .paymentType(paymentType)
                .requestId(requestId)
                .build();
    }

}
