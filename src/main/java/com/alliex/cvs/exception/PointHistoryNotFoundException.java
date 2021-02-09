package com.alliex.cvs.exception;

import org.springframework.http.HttpStatus;

public class PointHistoryNotFoundException extends InternalException {

    private static final long serialVersionUID = 3221324138277647185L;

    private static final HttpStatus httpStatus = HttpStatus.NOT_FOUND;

    private static final ErrorCode errorCode = ErrorCode.POINT_HISTORY_NOTFOUND;

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public PointHistoryNotFoundException(Long id) {
        super("Not found pointHistory. id: " + id);
    }

}