package com.alliex.cvs.config.security;

import com.alliex.cvs.config.security.util.AuthoritiesUtils;
import org.apache.commons.lang3.StringUtils;
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


/**
 * UserDetailsService를 구현한 UserDetailsServiceImpl과 기본 DaoAuthenticationProvider를 이용한 폼기반 인증도 있지만,
 * 이 클래스처럼 직접 AuthenticationProvider를 구현하여 UserDetailsService를 구현하지 않아도 되는 폼기반 인증도 있다.
 *
 * @author yun-yeoseong
 */
@Component
public class CustomUserAuthenticationProvider implements AuthenticationProvider {

    private UserDetailsService userDetailsService;
    private PasswordEncoder encoder;

    public CustomUserAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder encoder) {
        this.userDetailsService = userDetailsService;
        this.encoder = encoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        System.out.println("CustomUserAuthenticationProvider.authenticate ::::");
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;

        String userId = token.getName();

        LoginUser user = new LoginUser();
        User findUser = null;

        if (!StringUtils.isEmpty(userId)) {
            findUser = (User) userDetailsService.loadUserByUsername(userId);
        }

        if (ObjectUtils.isEmpty(findUser)) {
            throw new UsernameNotFoundException("Invalid username");
        }
        user.setUsername(findUser.getUsername());
        user.setPassword(findUser.getPassword());

        String password = user.getPassword();
        System.out.println("입력된 패스워드 = " + password + " 토큰 패스워드 = " + token.getCredentials().toString());
        if (!StringUtils.equals(password, encoder.encode(String.valueOf(token.getCredentials())))) {
            throw new BadCredentialsException("Invalid password");
        }

        Collection<? extends GrantedAuthority> authorities = AuthoritiesUtils.createAuthorities(user);

        return new UsernamePasswordAuthenticationToken(user, password, authorities);

    }

    @Override
    public boolean supports(Class<?> authentication) {
        // TODO Auto-generated method stub
        System.out.println("CustomUserAuthenticationProvider.supports ::::");
        return UsernamePasswordAuthenticationToken
                .class.equals(authentication);
    }

}
