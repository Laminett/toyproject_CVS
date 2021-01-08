package com.alliex.cvs.exception;

import org.springframework.http.HttpStatus;

public class NotEnoughPointException extends InternalException {

    private static final long serialVersionUID = -5184025018275659022L;

    private static final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

    private static final ErrorCode errorCode = ErrorCode.NOT_ENOUGH_POINT;

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public NotEnoughPointException(Long userPoint, Long requiredPoint) {
        super("Not Enough point : have point " + userPoint + " required point " + requiredPoint);
    }

}
