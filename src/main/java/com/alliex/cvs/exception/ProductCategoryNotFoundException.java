package com.alliex.cvs.exception;

import org.springframework.http.HttpStatus;

public class ProductCategoryNotFoundException extends InternalException {

    private static final long serialVersionUID = 2628492979112401018L;

    private static final HttpStatus httpStatus = HttpStatus.NOT_FOUND;

    private static final ErrorCode errorCode = ErrorCode.PRODUCT_CATEGORY_NOT_FOUND;

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public ProductCategoryNotFoundException(String productCategory) {
        super("Product Category" + productCategory + "is already exists.");
    }

}
