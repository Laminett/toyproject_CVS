package com.alliex.cvs.service;

import com.alliex.cvs.web.dto.UserResponse;
import com.alliex.cvs.web.dto.UserSaveRequest;
import com.alliex.cvs.web.dto.UserUpdateRequest;
import org.apache.commons.lang3.RandomStringUtils;
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
    public void createUser() {
        UserSaveRequest userSaveRequest = UserSaveRequest.builder()
                .username("test " + RandomStringUtils.randomAlphanumeric(5))
                .password("test")
                .department("marketing")
                .fullName("fullName " + RandomStringUtils.randomAlphanumeric(5))
                .email(RandomStringUtils.randomAlphanumeric(5) + "@email.com")
                .phoneNumber("010-1111-2222")
                .build();

        Long savedId = userService.save(userSaveRequest);
        System.out.println("savedId:" + savedId);

        assertThat(savedId).isNotNull();
    }

    @Test
    public void updateUser() {
        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder()
                .department("marketing updated")
                .fullName("fullName updated")
                .email(RandomStringUtils.randomAlphanumeric(5) + "-updated@email.com")
                .phoneNumber("010-1111-3333")
                .build();

        Long updatedId = userService.update(4L, userUpdateRequest);
        System.out.println("updatedId:" + updatedId);

        assertThat(updatedId).isNotNull();
    }

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

    @Test
    public void getUserById() {
        Long id = 1L;
        UserResponse userResponse = userService.getUserById(id);

        assertThat(userResponse.getId()).isEqualTo(id);
        assertThat(userResponse.getUsername()).isEqualTo("test");
    }

}