package com.alliex.cvs.web.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LoginResponse {

    private final Long id;

    private final String username;

    private final String token;

}
