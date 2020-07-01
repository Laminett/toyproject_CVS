package com.alliex.cvs.config.security.util;

import com.alliex.cvs.config.security.LoginUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.Collection;
import java.util.List;


public final class AuthoritiesUtils {

    private static final List<GrantedAuthority> ADMIN_ROLES = AuthorityUtils.createAuthorityList("ROLE_ADMIN", "ROLE_USER");
    private static final List<GrantedAuthority> USER_ROLES = AuthorityUtils.createAuthorityList("ROLE_USER");

    public static Collection<? extends GrantedAuthority> createAuthorities(LoginUser loginUser) {
        String username = loginUser.getUsername();
        if (username.startsWith("admin")) {
            return ADMIN_ROLES;
        }

        return USER_ROLES;
    }

}
