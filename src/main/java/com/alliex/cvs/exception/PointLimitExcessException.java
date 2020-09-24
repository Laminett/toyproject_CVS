package com.alliex.cvs.exception;

import org.springframework.http.HttpStatus;

public class PointLimitExcessException extends InternalException {

    private static final long serialVersionUID = -9021862414867821793L;

    private static final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

    private static final ErrorCode errorCode = ErrorCode.POINT_LIMIT_EXCESS;

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public PointLimitExcessException(String message) {
        super(message);
    }

}
