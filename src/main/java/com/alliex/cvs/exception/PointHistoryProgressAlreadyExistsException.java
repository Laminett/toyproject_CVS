package com.alliex.cvs.exception;

import org.springframework.http.HttpStatus;

public class PointHistoryProgressAlreadyExistsException extends InternalException {

    private static final long serialVersionUID = -6327294023923005383L;

    private static final HttpStatus httpStatus = HttpStatus.CONFLICT;

    private static final ErrorCode errorCode = ErrorCode.POINT_HISTORY_PROGRESS_ALREADY_EXISTS;

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public PointHistoryProgressAlreadyExistsException() {
        super("Another charge request already exists.");
    }

}