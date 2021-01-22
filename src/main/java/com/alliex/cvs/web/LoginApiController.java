package com.alliex.cvs.web;

import com.alliex.cvs.exception.LoginFailedException;
import com.alliex.cvs.security.ApiUserAuthenticationToken;
import com.alliex.cvs.service.UserService;
import com.alliex.cvs.web.dto.AuthenticationResult;
import com.alliex.cvs.web.dto.LoginRequest;
import com.alliex.cvs.web.dto.LoginResponse;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class LoginApiController {

    private final AuthenticationManager authenticationManager;

    private final UserService userService;

    @ApiOperation(value = "Authentication For API", notes = "앱 로그인")
    @PostMapping("/api/login")
    public LoginResponse authentication(@RequestBody @Valid LoginRequest loginRequest) {
        try {
            // Login.
            Authentication authentication = new ApiUserAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
            authentication = authenticationManager.authenticate(authentication);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Get Authentication.
            AuthenticationResult authenticationResult = (AuthenticationResult) authentication.getDetails();

            // Get User ID
            Long userId = userService.findByUsername(authenticationResult.getUser().getUsername()).get().getId();

            return new LoginResponse(userId, authenticationResult.getUser().getUsername(), authenticationResult.getApiToken());
        } catch (AuthenticationException e) {
            throw new LoginFailedException(loginRequest.getUsername(), e);
        }
    }

    // ToDo Refresh Token 구현 필요

}