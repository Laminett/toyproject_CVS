package com.alliex.cvs.config.security;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Id;
import java.util.Collection;

@Getter
@Setter
@ToString
public class LoginUser implements UserDetails {

    private static final long serialVersionUID = -4608347932140057654L;

    @Id
    private Long id;
    private String username;
    private String password;

    private Collection<? extends GrantedAuthority> authorities;
    private boolean accountNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean credentialsNonExpired = true;
    private boolean enabled = true;

}
