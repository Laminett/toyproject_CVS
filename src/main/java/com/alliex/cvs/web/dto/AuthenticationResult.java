package com.alliex.cvs.web.dto;

import com.alliex.cvs.domain.user.ApiUser;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthenticationResult {

    private String apiToken;

    private ApiUser user;

}
