package com.alliex.cvs.web;

import com.alliex.cvs.exception.UnauthorizedException;
import com.alliex.cvs.security.ApiUserAuthenticationToken;
import com.alliex.cvs.web.dto.AuthenticationResult;
import com.alliex.cvs.web.dto.LoginRequest;
import com.alliex.cvs.web.dto.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

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
            throw new UnauthorizedException(e.getMessage());
        }
    }

}