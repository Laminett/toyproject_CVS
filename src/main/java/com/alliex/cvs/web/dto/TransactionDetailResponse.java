package com.alliex.cvs.web.dto;

import lombok.Getter;

@Getter
public class TransactionDetailResponse {

    private Long id;

    private Long productId;

    private String productName;

    private Long transactionId;

    private Long productPoint;

    private Integer productQuantity;

    public TransactionDetailResponse(Long id, Long productId, String productName, Long productPoint, Integer productQuantity, Long transactionId) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.productPoint = productPoint;
        this.productQuantity = productQuantity;
        this.transactionId = transactionId;
    }

}
