package com.alliex.cvs.exception;

import org.springframework.http.HttpStatus;

public class ProductNotFoundException extends InternalException {

    private static final long serialVersionUID = -4453625322924754581L;

    private static final HttpStatus httpStatus = HttpStatus.NOT_FOUND;

    private static final ErrorCode errorCode = ErrorCode.PRODUCT_NOT_FOUND;

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public ProductNotFoundException(Long id) {
        super("Product id " + id + " does not exist.");
    }

    public ProductNotFoundException(String barcode) {
        super("Product barcode " + barcode + " does not exitst.");
    }

}
