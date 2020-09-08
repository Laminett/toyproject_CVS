package com.alliex.cvs.security;

import com.alliex.cvs.web.dto.ApiResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    public static ApiResult E403 = new ApiResult("Authentication error (cause: forbidden)", HttpStatus.FORBIDDEN);

    @Autowired
    private ObjectMapper om;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setHeader("content-type", "application/json");
        response.getWriter().write(om.writeValueAsString(E403));
        response.getWriter().flush();
        response.getWriter().close();
    }

}