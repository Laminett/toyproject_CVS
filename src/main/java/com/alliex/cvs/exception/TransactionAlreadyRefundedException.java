package com.alliex.cvs.exception;

import org.springframework.http.HttpStatus;

public class TransactionAlreadyRefundedException extends InternalException {

    private static final long serialVersionUID = -6300364298499675092L;

    private static final HttpStatus httpStatus = HttpStatus.CONFLICT;

    private static final ErrorCode errorCode = ErrorCode.TRANSACTION_ALREADY_REFUNDED;

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public TransactionAlreadyRefundedException(String requestId) {
        super("Transaction " + requestId + " is already refunded.");
    }

}
