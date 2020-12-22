package com.alliex.cvs.exception;

import org.springframework.http.HttpStatus;

public class ProductPurchaseNotFoundException extends InternalException {

    private static final long serialVersionUID = -7133336387629327844L;

    private static final HttpStatus httpStatus = HttpStatus.NOT_FOUND;

    private static final ErrorCode errorCode = ErrorCode.PRODUCT_PURCHASE_NOT_FOUND;

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public ProductPurchaseNotFoundException(Long id) {
        super("ProductPurchase id " + id + " does not exist.");
    }

}
