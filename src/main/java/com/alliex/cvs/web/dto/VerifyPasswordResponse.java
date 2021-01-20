package com.alliex.cvs.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class VerifyPasswordResponse {

    private String password;

    private boolean isValid;

}