package com.alliex.cvs.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

public class LoginFailedException extends InternalException {

    private static final long serialVersionUID = 3308305388863052725L;

    private static final HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;

    private static final ErrorCode errorCode = ErrorCode.LOGIN_FAILED;

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public LoginFailedException(String username, AuthenticationException e) {
        super("User " + username + " login failed.", e);
    }

}
