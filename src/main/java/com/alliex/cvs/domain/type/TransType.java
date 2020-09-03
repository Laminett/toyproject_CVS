package com.alliex.cvs.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TransType {

    PAYMENT("payment"),
    REFUND("refund");

    private final String value;

}
