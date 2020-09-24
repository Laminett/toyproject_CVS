package com.alliex.cvs.web;

import com.alliex.cvs.exception.LoginFailedException;
import com.alliex.cvs.security.ApiUserAuthenticationToken;
import com.alliex.cvs.web.dto.AuthenticationResult;
import com.alliex.cvs.web.dto.LoginRequest;
import com.alliex.cvs.web.dto.LoginResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginApiController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @ApiOperation(value = "Authentication For API", notes = "앱 로그인")
    @PostMapping("api/login")
    public LoginResponse authentication(@RequestBody LoginRequest authRequest) {
        try {
            // Login.
            Authentication authentication = new ApiUserAuthenticationToken(authRequest.getUsername(), authRequest.getPassword());
            authentication = authenticationManager.authenticate(authentication);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Get Authentication.
            AuthenticationResult authenticationResult = (AuthenticationResult) authentication.getDetails();

            return new LoginResponse(authenticationResult.getUser().getUsername(), authenticationResult.getApiToken());
        } catch (AuthenticationException e) {
            throw new LoginFailedException(authRequest.getUsername(), e);
        }
    }

    // ToDo Refresh Token 구현 필요

}