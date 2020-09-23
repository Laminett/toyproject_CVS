package com.alliex.cvs.exception;

public class NotEnoughPointException extends RuntimeException {

    public NotEnoughPointException(String message) {
        super(message);
    }

    public NotEnoughPointException(Throwable cause) {
        super("Not Enough Point.", cause);
    }

    public NotEnoughPointException(String message, Throwable cause) {
        super(message, cause);
    }

}
