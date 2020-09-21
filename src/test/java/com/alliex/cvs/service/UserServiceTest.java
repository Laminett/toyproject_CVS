package com.alliex.cvs.service;

import com.alliex.cvs.domain.type.Role;
import com.alliex.cvs.web.dto.UserRequest;
import com.alliex.cvs.web.dto.UserResponse;
import com.alliex.cvs.web.dto.UserSaveRequest;
import com.alliex.cvs.web.dto.UserUpdateRequest;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional()
@Rollback()
public class UserServiceTest {
    @Autowired
    UserService userService;

    @Test
    public void createUser() {
        String username = "test " + RandomStringUtils.randomAlphanumeric(5);
        String password = "password";
        String department = "department";
        String fullName = "fullName " + RandomStringUtils.randomAlphanumeric(5);
        String email = RandomStringUtils.randomAlphanumeric(5) + "@email.com";
        String phoneNumber = "010-1111-2222";
        Role role = Role.ADMIN;

        // Create user.
        UserSaveRequest userSaveRequest = UserSaveRequest.builder()
                .username(username)
                .password(password)
                .department(department)
                .fullName(fullName)
                .email(email)
                .phoneNumber(phoneNumber)
                .role(role)
                .build();

        Long savedId = userService.save(userSaveRequest);
        assertThat(savedId).isNotNull();

        // Get user.
        UserResponse userResponse = userService.getUserById(savedId);
        assertThat(userResponse.getUsername()).isEqualTo(username);
        assertThat(userResponse.getDepartment()).isEqualTo(department);
        assertThat(userResponse.getFullName()).isEqualTo(fullName);
        assertThat(userResponse.getEmail()).isEqualTo(email);
        assertThat(userResponse.getPhoneNumber()).isEqualTo(phoneNumber);
        assertThat(userResponse.getRole()).isEqualTo(role);
    }

    @Test
    public void updateUser() {
        // Update user.
        UserUpdateRequest userUpdateRequest = new UserUpdateRequest();
        userUpdateRequest.setDepartment("department updated");
        userUpdateRequest.setFullName("fullName updated");
        userUpdateRequest.setEmail("email updated");
        userUpdateRequest.setPhoneNumber("phoneNumber updated");
        Long updatedId = userService.update(1L, userUpdateRequest);

        // Get user.
        UserResponse userResponse = userService.getUserById(updatedId);
        assertThat(userResponse.getDepartment()).isEqualTo("department updated");
        assertThat(userResponse.getFullName()).isEqualTo("fullName updated");
        assertThat(userResponse.getEmail()).isEqualTo("email updated");
        assertThat(userResponse.getPhoneNumber()).isEqualTo("phoneNumber updated");
    }

    @Test
    public void getUsers() {
        List<UserResponse> users = userService.getUsers(PageRequest.of(0, 10));

        assertThat(users.size()).isGreaterThanOrEqualTo(1);
    }

    @Test
    public void getUsersByFullName() {
        String fullName = "admin";
        UserRequest userRequest = new UserRequest();
        userRequest.setFullName(fullName);

        Page<UserResponse> users = userService.getUsers(PageRequest.of(0, 10), userRequest);
        users.getContent().forEach(userResponse -> {
            assertThat(userResponse.getFullName()).isEqualTo(fullName);
        });
    }

    @Test
    public void getUsersByEmail() {
        String email = "test@test.com";
        UserRequest userRequest = new UserRequest();
        userRequest.setEmail(email);

        Page<UserResponse> users = userService.getUsers(PageRequest.of(0, 10), userRequest);

        assertThat(users.getContent().size()).isGreaterThanOrEqualTo(1);
        users.getContent().forEach(userResponse -> {
            assertThat(userResponse.getEmail()).contains(email);
        });
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