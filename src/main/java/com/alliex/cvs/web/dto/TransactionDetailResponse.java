package com.alliex.cvs.web.dto;

import lombok.Getter;

@Getter
public class TransactionDetailResponse {

    private Long id;

    private Long productId;

    private String productName;

    private Long transId;

    private Integer productPoint;

    private Integer productAmount;

    public TransactionDetailResponse(Long id, Long productId, String productName, Integer productPoint, Integer productAmount, Long transId) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.productPoint = productPoint;
        this.productAmount = productAmount;
        this.transId = transId;
    }

}
