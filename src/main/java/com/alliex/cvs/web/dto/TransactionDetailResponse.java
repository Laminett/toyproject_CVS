package com.alliex.cvs.web.dto;

import lombok.Getter;

@Getter
public class TransactionDetailResponse {

    private Long id;

    private Long productId;

    private String productName;

    private String requestId;

    private Long productPoint;

    private Integer productQuantity;

    public TransactionDetailResponse(Long id, Long productId, String productName, Long productPoint, Integer productQuantity, String requestId) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.requestId = requestId;
        this.productPoint = productPoint;
        this.productQuantity = productQuantity;
    }

}
