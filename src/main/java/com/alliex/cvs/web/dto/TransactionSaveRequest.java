package com.alliex.cvs.web.dto;

import com.alliex.cvs.domain.transaction.TransState;
import com.alliex.cvs.domain.transaction.TransType;
import com.alliex.cvs.domain.transaction.Transaction;
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

    private Integer transPoint;

    private TransState transState;

    private TransType transType;

    private String transNumber;

    private List<Map<String, String>> transProduct;

    @Builder
    public TransactionSaveRequest(Long buyerId, Long merchantId, Long originId, Integer transPoint, TransState transState, TransType transType, String transNumber, List<Map<String, String>> transProduct) {
        this.buyerId = buyerId;
        this.merchantId = merchantId;
        this.originId = originId;
        this.transPoint = transPoint;
        this.transState = transState;
        this.transType = transType;
        this.transNumber = transNumber;
        this.transProduct = transProduct;
    }

    @Builder
    public TransactionSaveRequest(Long buyerId, Long merchantId, Long originId, Integer transPoint, TransState transState, TransType transType, String transNumber) {
        this.buyerId = buyerId;
        this.merchantId = merchantId;
        this.originId = originId;
        this.transPoint = transPoint;
        this.transState = transState;
        this.transType = transType;
        this.transNumber = transNumber;
    }

    public Transaction toEntity() {
        return Transaction.builder()
                .buyerId(buyerId)
                .merchantId(merchantId)
                .originId(originId)
                .transPoint(transPoint)
                .transState(transState)
                .transType(transType)
                .transNumber(transNumber)
                .build();
    }

}
