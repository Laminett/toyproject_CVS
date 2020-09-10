package com.alliex.cvs.service;

import com.alliex.cvs.web.dto.UserResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    UserService userService;

    @Test
    public void getUsers() {
        List<UserResponse> users = userService.getUsers();

        assertThat(users.size()).isGreaterThanOrEqualTo(1);
    }

    @Test
    public void getUserByUsername() {
        String username = "test";
        UserResponse user = userService.getUserByUsername(username);

        assertThat(user.getUsername()).isEqualTo(username);
    }

}