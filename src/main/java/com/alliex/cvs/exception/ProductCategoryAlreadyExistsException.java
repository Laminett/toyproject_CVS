package com.alliex.cvs.exception;

import org.springframework.http.HttpStatus;

public class ProductCategoryAlreadyExistsException extends InternalException {

    private static final long serialVersionUID = 560777128966704076L;

    private static final HttpStatus httpStatus = HttpStatus.CONFLICT;

    private static final ErrorCode errorCode = ErrorCode.PRODUCT_CATEGORY_ALREADY_EXISTS;

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public ProductCategoryAlreadyExistsException(String productCategory) {
        super("Product Category " + productCategory + " is already exists.");
    }

}
