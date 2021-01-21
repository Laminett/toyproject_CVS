package com.alliex.cvs.web.dto;

import com.alliex.cvs.domain.type.PaymentType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TransactionSaveRequest {

    private PaymentType paymentType;

    private String requestId;

    private List<TransactionDetailSaveRequest> transProduct;

}
