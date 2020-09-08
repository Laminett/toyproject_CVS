package com.alliex.cvs.exception;

public class UserNotFoundException extends RuntimeException {

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
