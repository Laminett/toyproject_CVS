package com.alliex.cvs.testsupport;

import com.alliex.cvs.domain.user.LoginUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Arrays;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        LoginUser user = new LoginUser();
        user.setUsername(customUser.username());
        user.setPassword(customUser.password());

        Authentication auth = new UsernamePasswordAuthenticationToken(user, "password", Arrays.asList(new SimpleGrantedAuthority(customUser.role())));
        context.setAuthentication(auth);

        return context;
    }

}