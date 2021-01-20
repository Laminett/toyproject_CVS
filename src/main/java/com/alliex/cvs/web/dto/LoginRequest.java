package com.alliex.cvs.web.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class LoginRequest {

    @NotBlank(message = "username may not be blank.")
    private String username;

    @NotBlank(message = "password may not be blank.")
    private String password;

}
