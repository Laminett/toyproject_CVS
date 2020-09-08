package com.alliex.cvs.exception;

public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(Throwable cause) {
        super("User Not Found.", cause);
    }

    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }


}
