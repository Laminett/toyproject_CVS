package com.alliex.cvs.exception;

public class PointHistoryNotFoundException extends RuntimeException {

    public PointHistoryNotFoundException(String message) {
        super(message);
    }

    public PointHistoryNotFoundException(Throwable cause) {
        super("Point History Not Found.", cause);
    }

    public PointHistoryNotFoundException(String messgae, Throwable cause) {
        super(messgae, cause);
    }

}