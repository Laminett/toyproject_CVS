package com.alliex.cvs.exception;

public class TransactionNotFoundException extends RuntimeException {

    public TransactionNotFoundException(String message) {
        super(message);
    }

    public TransactionNotFoundException(Throwable cause) {
        super("Transaction Not Found.", cause);
    }

    public TransactionNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
