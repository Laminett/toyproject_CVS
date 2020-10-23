package com.alliex.cvs.exception;

import org.springframework.http.HttpStatus;

public class TransactionDetailNotFoundException extends InternalException {

    private static final long serialVersionUID = -7825530560233055360L;

    private static final HttpStatus httpStatus = HttpStatus.NOT_FOUND;

    private static final ErrorCode errorCode = ErrorCode.TRANSACTION_DETAIL_NOT_FOUND;

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public TransactionDetailNotFoundException(Long transId) {
        super("TransactionDetail id " + transId + " does not exist.");
    }

}