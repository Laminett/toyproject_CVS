package com.alliex.cvs.exception;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class ErrorResponse {

    private ErrorCode code;

    private String message;

}
