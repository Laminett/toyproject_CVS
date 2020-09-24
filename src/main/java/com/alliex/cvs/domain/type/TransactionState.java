package com.alliex.cvs.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TransactionState {

    SUCCESS,
    FAIL,
    WAIT,
    REFUND

}


