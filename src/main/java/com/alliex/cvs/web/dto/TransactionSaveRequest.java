package com.alliex.cvs.web.dto;

import com.alliex.cvs.domain.type.PaymentType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
public class TransactionSaveRequest {

    private PaymentType paymentType;

    private String requestId;

    private List<Map<String, String>> transProduct;

    @Builder
    public TransactionSaveRequest(String requestId, List<Map<String, String>> transProduct, PaymentType paymentType) {
        this.requestId = requestId;
        this.transProduct = transProduct;
        this.paymentType = paymentType;
    }

}
