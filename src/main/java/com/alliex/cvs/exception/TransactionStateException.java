package com.alliex.cvs.exception;

import org.springframework.http.HttpStatus;

public class TransactionStateException extends InternalException {

    private static final long serialVersionUID = -2191271147187065855L;

    private static final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

    private static final ErrorCode errorCode = ErrorCode.TRANSACTION_STATE_ERROR;

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public TransactionStateException(String message) {
        super(message);
    }

}
