package com.alliex.cvs.exception;

import org.springframework.http.HttpStatus;

public class TransactionNotFoundException extends InternalException {

    private static final long serialVersionUID = 4482193346858541197L;

    private static final HttpStatus httpStatus = HttpStatus.NOT_FOUND;

    private static final ErrorCode errorCode = ErrorCode.TRANSACTION_NOT_FOUND;

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public TransactionNotFoundException(String message) {
        super(message);
    }

}
