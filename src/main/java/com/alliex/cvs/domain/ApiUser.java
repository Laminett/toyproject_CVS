package com.alliex.cvs.domain;

import com.alliex.cvs.security.JWT;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "username")
public class ApiUser {

    private String username;

    public ApiUser(String username) {
        this.username = username;
    }

    public String generateToken(JWT jwt, Collection<? extends GrantedAuthority> roles) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);
        claims.put("roles", roles);

        return jwt.generateToken(claims);
    }

}