package com.alliex.cvs.exception;

public enum ErrorCode {

    // Common.
    INTERNAL_ERROR,
    ACCESS_DENIED,
    BAD_CREDENTIAL,
    INVALID_REQUEST,
    REQUEST_ALREADY_EXISTS,

    // User.
    USER_NOT_FOUND,
    USER_ALREADY_EXISTS,

}