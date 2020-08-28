package com.alliex.cvs.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
public class TransactionDetailRequest {

    private Long buyerId;

    private Long merchantId;

    private List<Map<String, String>> transProduct;

    private String transPoint;

    @Builder
    public TransactionDetailRequest(Long buyerId, Long merchantId, List<Map<String, String>> transProduct, String transPoint) {
        this.buyerId = buyerId;
        this.merchantId = merchantId;
        this.transProduct = transProduct;
        this.transPoint = transPoint;
    }

}
