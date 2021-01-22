package com.alliex.cvs.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TransactionRequest {

    private String userId;

    private String paymentType;

    private Long point;

    private String state;

    private String type;

}
