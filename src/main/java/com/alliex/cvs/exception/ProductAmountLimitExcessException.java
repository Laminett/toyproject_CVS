package com.alliex.cvs.exception;

import org.springframework.http.HttpStatus;

public class ProductAmountLimitExcessException extends InternalException {

    private static final long serialVersionUID = 2862231333608260345L;

    private static final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

    private static final ErrorCode errorCode = ErrorCode.PRODUCT_AMOUNT_LIMIT_EXCESS;

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public ProductAmountLimitExcessException(int quantity) {
        super("Not enough Product amount. amount :" + quantity);
    }

}
