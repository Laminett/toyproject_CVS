package com.alliex.cvs.exception;

import org.springframework.http.HttpStatus;

public class SettleNotFoundException extends InternalException {

    private static final long serialVersionUID = 8668519418563497303L;

    private static final HttpStatus httpStatus = HttpStatus.NOT_FOUND;

    private static final ErrorCode errorCode = ErrorCode.SETTLE_NOT_FOUND;

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public ErrorCode getErrorCode() {
        return errorCode;
    }

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