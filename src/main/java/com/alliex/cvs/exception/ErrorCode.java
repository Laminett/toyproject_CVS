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
    LOGIN_FAILED,

    // Transaction.
    TRANSACTION_NOT_FOUND,
    TRANSACTION_ALREADY_EXISTS,

    // Settle.
    SETTLE_NOT_FOUND,

    // Product.
    PRODUCT_NOT_FOUND,
    PRODUCT_AMOUNT_LIMIT_EXCESS,

    // Product Category
    PRODUCT_CATEGORY_ALREADY_EXISTS,
    PRODUCT_CATEGORY_NOT_FOUND,

    // Point.
    POINT_LIMIT_EXCESS,
    POINT_HISTORY_NOTFOUND,


}