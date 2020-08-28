package com.alliex.cvs.domain.transaction;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TransState {

    SUCCESS("success"),
    FAIL("fail"),
    WAIT("wait"),
    REFUND("refund");

    private final String value;

}


