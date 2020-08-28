package com.alliex.cvs.domain.transaction;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TransType {

    PAYMENT("payment"),
    REFUND("refund");

    private final String value;

}
