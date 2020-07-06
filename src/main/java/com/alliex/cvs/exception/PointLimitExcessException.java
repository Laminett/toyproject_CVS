package com.alliex.cvs.exception;

public class PointLimitExcessException extends RuntimeException{

    public PointLimitExcessException(String message) {
        super(message);
    }

    public PointLimitExcessException(Throwable cause) {
        super("Point Limit Excess.", cause);
    }

    public PointLimitExcessException(String message, Throwable cause) {
        super(message, cause);
    }

}
