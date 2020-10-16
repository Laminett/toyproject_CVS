package com.alliex.cvs.exception;

import org.springframework.http.HttpStatus;

public class ProductAlreadyExistsException extends InternalException {

    private static final long serialVersionUID = -8112408747274403425L;

    private static final HttpStatus httpStatus = HttpStatus.CONFLICT;

    private static final ErrorCode errorCode = ErrorCode.PRODUCT_ALREADY_EXISTS;

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public ProductAlreadyExistsException(String productName) {
        super("Product name: " + productName + " is already exists.");
    }

}
