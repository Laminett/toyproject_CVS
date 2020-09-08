package com.alliex.cvs.web.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiError {

    private int status;

    private String message;

    public ApiError(Throwable throwable, HttpStatus status) {
        this(throwable.getMessage(), status);
    }

    public ApiError(String message, HttpStatus status) {
        this.message = message;
        this.status = status.value();
    }

}