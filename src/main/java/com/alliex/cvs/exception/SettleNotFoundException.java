package com.alliex.cvs.exception;

public class SettleNotFoundException extends RuntimeException {

    public SettleNotFoundException(String message) {
        super(message);
    }

    public SettleNotFoundException(Throwable cause) {
        super("Settle Not Found.", cause);
    }

    public SettleNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}