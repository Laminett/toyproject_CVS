package com.alliex.cvs.service;

import com.alliex.cvs.domain.user.User;
import com.alliex.cvs.domain.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public Optional<User> findByUsername(String username) {
        return repository.findByUsername(username);
    }

}
