package com.alliex.cvs.web.dto;

import com.alliex.cvs.domain.type.PaymentType;
import com.alliex.cvs.domain.type.TransactionState;
import com.alliex.cvs.domain.type.TransactionType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TransactionSave {

    private Long userId;

    private String originRequestId;

    private Long point;

    private PaymentType paymentType;

    private TransactionState state;

    private TransactionType type;

    private String requestId;

}
