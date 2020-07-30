package com.alliex.cvs.exception;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(String message) {
        super(message);
    }

    public ProductNotFoundException(Throwable cause) {
        super("Point Limit Excess.", cause);
    }

    public ProductNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
