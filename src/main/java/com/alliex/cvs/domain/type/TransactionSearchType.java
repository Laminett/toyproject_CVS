package com.alliex.cvs.domain.type;

import lombok.Getter;

@Getter
public enum TransactionSearchType {

    ID("id"),
    USERID("user"),
    MERCHANTID("merchantId"),
    PAYMENTTYPE("paymentType"),
    POINT("point"),
    STATE("state"),
    TYPE("type");

    private final String value;

    TransactionSearchType(String value) {
        this.value = value;
    }

}
