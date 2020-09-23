package com.alliex.cvs.web.dto;

import com.alliex.cvs.domain.type.TransactionPaymentType;
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

    private Integer point;

    private TransactionPaymentType paymentType;

    private TransactionState state;

    private TransactionType type;

    private String requestId;

    private List<Map<String, String>> transProduct;

    @Builder
    public TransactionSaveRequest(Long userId, Long merchantId, Long originId, Integer point, TransactionState state, TransactionType type, String requestId, List<Map<String, String>> transProduct) {
        this.userId = userId;
        this.merchantId = merchantId;
        this.originId = originId;
        this.point = point;
        this.state = state;
        this.type = type;
        this.requestId = requestId;
        this.transProduct = transProduct;
    }

    @Builder
    public TransactionSaveRequest(Long userId, Long merchantId, Long originId, Integer point, TransactionState state, TransactionType type, String requestId, TransactionPaymentType paymentType) {
        this.userId = userId;
        this.merchantId = merchantId;
        this.originId = originId;
        this.point = point;
        this.state = state;
        this.type = type;
        this.requestId = requestId;
        this.paymentType = paymentType;
    }

    public Transaction toEntity() {
        User setUserId = new User();
        setUserId.setId(userId);

        return Transaction.builder()
                .user(setUserId)
                .merchantId(merchantId)
                .originId(originId)
                .point(point)
                .state(state)
                .type(type)
                .requestId(requestId)
                .build();
    }

}
