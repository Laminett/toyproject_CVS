package com.alliex.cvs.web.dto;

import com.alliex.cvs.domain.type.TransState;
import com.alliex.cvs.domain.type.TransType;
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

    private Long buyerId;

    private Long merchantId;

    private Long originId;

    private Integer point;

    private TransState transState;

    private TransType transType;

    private String transNumber;

    private List<Map<String, String>> transProduct;

    @Builder
    public TransactionSaveRequest(Long buyerId, Long merchantId, Long originId, Integer point, TransState transState, TransType transType, String transNumber, List<Map<String, String>> transProduct) {
        this.buyerId = buyerId;
        this.merchantId = merchantId;
        this.originId = originId;
        this.point = point;
        this.transState = transState;
        this.transType = transType;
        this.transNumber = transNumber;
        this.transProduct = transProduct;
    }

    @Builder
    public TransactionSaveRequest(Long buyerId, Long merchantId, Long originId, Integer point, TransState transState, TransType transType, String transNumber) {
        this.buyerId = buyerId;
        this.merchantId = merchantId;
        this.originId = originId;
        this.point = point;
        this.transState = transState;
        this.transType = transType;
        this.transNumber = transNumber;
    }

    public Transaction toEntity() {
        User setUserId = new User();
        setUserId.setId(buyerId);

        return Transaction.builder()
                .user(setUserId)
                .merchantId(merchantId)
                .originId(originId)
                .point(point)
                .transState(transState)
                .transType(transType)
                .transNumber(transNumber)
                .build();
    }

}
