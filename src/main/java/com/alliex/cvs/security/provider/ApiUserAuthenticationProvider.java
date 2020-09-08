package com.alliex.cvs.security.provider;

import com.alliex.cvs.domain.user.ApiUser;
import com.alliex.cvs.exception.UserNotFoundException;
import com.alliex.cvs.security.ApiUserAuthenticationToken;
import com.alliex.cvs.security.JWT;
import com.alliex.cvs.web.dto.AuthenticationResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Collection;

@Component
public class ApiUserAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private JWT jwt;

    @Qualifier("userService")
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String principal = (String) authentication.getPrincipal();
        String credentials = (String) authentication.getCredentials();

        try {
            User findUser = (User) userDetailsService.loadUserByUsername(principal);
            if (ObjectUtils.isEmpty(findUser)) {
                throw new UsernameNotFoundException("User " + principal + " does not exist.");
            }

            if (!StringUtils.equals(findUser.getPassword(), encoder.encode(String.valueOf(credentials)))) {
                throw new BadCredentialsException("Invalid password");
            }

            ApiUser user = new ApiUser(findUser.getUsername());

            ApiUserAuthenticationToken authenticated = new ApiUserAuthenticationToken(user.getUsername(), null, generateAuthorities());
            String apiToken = user.generateToken(jwt, generateAuthorities());
            authenticated.setDetails(new AuthenticationResult(apiToken, user));

            return authenticated;
        } catch (UserNotFoundException e) {
            throw new UsernameNotFoundException(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new BadCredentialsException(e.getMessage());
        } catch (Exception e) {
            throw new AuthenticationServiceException(e.getMessage(), e);
        }
    }

    // ToDo 사용자 정보에서 가져오도록 변경
    private Collection<GrantedAuthority> generateAuthorities() {
        return AuthorityUtils.createAuthorityList("ROLE_USER");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return ApiUserAuthenticationToken.class.isAssignableFrom(authentication);
    }

}