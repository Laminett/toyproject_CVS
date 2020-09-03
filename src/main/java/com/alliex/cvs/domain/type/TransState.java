package com.alliex.cvs.domain.type;

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


