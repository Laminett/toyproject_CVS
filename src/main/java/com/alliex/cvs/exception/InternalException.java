package com.alliex.cvs.exception;

import org.springframework.http.HttpStatus;

public abstract class InternalException extends RuntimeException {

    private static final long serialVersionUID = -3371145921443563521L;

    public abstract HttpStatus getHttpStatus();

    public abstract ErrorCode getErrorCode();

    public InternalException(String message) {
        super(message);
    }

    public InternalException(String message, Throwable cause) {
        super(message, cause);
    }

}
