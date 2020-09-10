package com.alliex.cvs.service;

import com.alliex.cvs.domain.user.LoginUser;
import com.alliex.cvs.domain.user.User;
import com.alliex.cvs.domain.user.UserRepository;
import com.alliex.cvs.util.AuthoritiesUtils;
import com.alliex.cvs.web.dto.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    public Optional<User> findByUsername(String username) {
        return repository.findByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found." ));

        LoginUser loginUser = new LoginUser();
        loginUser.setUsername(username);

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), AuthoritiesUtils.createAuthorities(loginUser));
    }

    public List<UserResponse> getUsers() {
        return repository.findAll().stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
    }

    public UserResponse getUserByUsername(String username) {
        User user = findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found." ));

        return new UserResponse(user);
    }

}
