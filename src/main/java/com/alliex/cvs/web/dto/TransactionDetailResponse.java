package com.alliex.cvs.web.dto;

import com.alliex.cvs.domain.type.TransactionState;
import lombok.Getter;

@Getter
public class TransactionDetailResponse {

    private Long id;

    private Long productId;

    private String productName;

    private String requestId;

    private Long productPoint;

    private Integer productQuantity;

    private TransactionState transactionState;

    public TransactionDetailResponse(Long id, Long productId, String productName, Long productPoint, Integer productQuantity, String requestId, TransactionState transactionState) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.productPoint = productPoint;
        this.productQuantity = productQuantity;
        this.requestId = requestId;
        this.transactionState = transactionState;
    }

}
