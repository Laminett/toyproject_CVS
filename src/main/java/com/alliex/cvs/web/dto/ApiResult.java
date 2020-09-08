package com.alliex.cvs.web.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiResult<T> {

    private boolean success;

    private ApiError error;

    private T response;

    public ApiResult(T response) {
        this.response = response;
        this.success = true;
    }

    public ApiResult(String message, HttpStatus status) {
        this.success = false;
        this.response = null;
        this.error = new ApiError(message, status);
    }

    public ApiResult(Throwable throwable, HttpStatus status) {
        this.success = false;
        this.response = null;
        this.error = new ApiError(throwable, status);
    }

}