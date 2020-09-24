package com.alliex.cvs.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends InternalException {

    private static final long serialVersionUID = 1340181096918030004L;

    private static final HttpStatus httpStatus = HttpStatus.NOT_FOUND;

    private static final ErrorCode errorCode = ErrorCode.USER_NOT_FOUND;

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public UserNotFoundException() {
        this(null, null);
    }

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(Throwable cause) {
        super("User Not Found.", cause);
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
