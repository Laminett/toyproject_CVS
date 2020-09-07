package com.alliex.cvs.exception;

public class ProductAmountLimitExcessException extends RuntimeException {

    public ProductAmountLimitExcessException(String message) {
        super(message);
    }

    public ProductAmountLimitExcessException(Throwable cause) {
        super("Amount Limit Excess.", cause);
    }

    public ProductAmountLimitExcessException(String message, Throwable cause) {
        super(message, cause);
    }

}
