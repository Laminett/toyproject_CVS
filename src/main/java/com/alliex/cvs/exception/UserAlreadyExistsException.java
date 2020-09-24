package com.alliex.cvs.exception;

import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends InternalException {

    private static final long serialVersionUID = -3944130679216500939L;

    private static final HttpStatus httpStatus = HttpStatus.CONFLICT;

    private static final ErrorCode errorCode = ErrorCode.USER_ALREADY_EXISTS;

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public UserAlreadyExistsException(String username) {
        super("User " + username + " is already exists.");
    }

}
