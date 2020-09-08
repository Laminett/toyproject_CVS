package com.alliex.cvs.security.provider;

import com.alliex.cvs.domain.user.LoginUser;
import com.alliex.cvs.util.AuthoritiesUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Collection;


@Component
public class CustomUserAuthenticationProvider implements AuthenticationProvider {

    private UserDetailsService userDetailsService;
    private PasswordEncoder encoder;

    public CustomUserAuthenticationProvider(@Qualifier("userService") UserDetailsService userDetailsService, PasswordEncoder encoder) {
        this.userDetailsService = userDetailsService;
        this.encoder = encoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;

        User findUser = (User) userDetailsService.loadUserByUsername(token.getName());

        if (ObjectUtils.isEmpty(findUser)) {
            throw new UsernameNotFoundException("User " + token.getName() + " does not exist.");
        }

        LoginUser user = new LoginUser();
        user.setUsername(findUser.getUsername());
        user.setPassword(findUser.getPassword());

        String password = user.getPassword();
        if (!StringUtils.equals(password, encoder.encode(String.valueOf(token.getCredentials())))) {
            throw new BadCredentialsException("Invalid password");
        }

        Collection<? extends GrantedAuthority> authorities = AuthoritiesUtils.createAuthorities(user);

        return new UsernamePasswordAuthenticationToken(user, password, authorities);

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken
                .class.equals(authentication);
    }

}
