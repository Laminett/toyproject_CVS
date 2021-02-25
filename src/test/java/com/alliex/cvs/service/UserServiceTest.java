package com.alliex.cvs.service;

import com.alliex.cvs.domain.type.Role;
import com.alliex.cvs.web.dto.UserRequest;
import com.alliex.cvs.web.dto.UserResponse;
import com.alliex.cvs.web.dto.UserSaveRequest;
import com.alliex.cvs.web.dto.UserUpdateRequest;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional()
@Rollback()
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:test-data.sql"),
})
public class UserServiceTest {

    @Autowired
    UserService userService;

    private final String testUsername = "testtest100";
    private final String testEmail = "400@test.com";
    private final String testFullName = "400_fullName";

    @Test
    public void createAndGetUser() {
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

        UserResponse savedUserResponse = userService.save(userSaveRequest);
        assertThat(savedUserResponse.getId()).isNotNull();

        // Get user.
        UserResponse userResponse = userService.getUserById(savedUserResponse.getId());
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
        Long updatedId = userService.update(400L, userUpdateRequest);

        // Get user.
        UserResponse userResponse = userService.getUserById(updatedId);
        assertThat(userResponse.getDepartment()).isEqualTo("department updated");
        assertThat(userResponse.getFullName()).isEqualTo("fullName updated");
        assertThat(userResponse.getEmail()).isEqualTo("email updated");
        assertThat(userResponse.getPhoneNumber()).isEqualTo("phoneNumber updated");
    }

    @Test
    public void getUsers() {
        Page<UserResponse> users = userService.getUsers(PageRequest.of(0, 10), new UserRequest());
        assertThat(users.getTotalElements()).isGreaterThanOrEqualTo(1);

        users.forEach(item -> System.out.println(item.getPoint()));
    }

    @Test
    public void getUserByUsername() {
        UserResponse user = userService.getUserByUsername(testUsername);
        assertThat(user.getUsername()).isEqualTo(testUsername);
    }

    @Test
    public void getUsersByUsername() {
        UserRequest userRequest = new UserRequest();
        userRequest.setUsername("aaa");

        Page<UserResponse> users = userService.getUsers(PageRequest.of(0, 10), userRequest);
        users.getContent().forEach(userResponse -> {
            assertThat(userResponse.getFullName()).isEqualTo("aaa");
        });
    }

    @Test
    public void getUsersByFullName() {
        UserRequest userRequest = new UserRequest();
        userRequest.setFullName("fullName");

        Page<UserResponse> users = userService.getUsers(PageRequest.of(0, 10), userRequest);
        users.getContent().forEach(userResponse -> {
            assertThat(userResponse.getFullName()).contains("fullName");
        });
    }

    @Test
    public void getUsersByEmail() {
        UserRequest userRequest = new UserRequest();
        userRequest.setEmail("aaa");

        Page<UserResponse> users = userService.getUsers(PageRequest.of(0, 10), userRequest);

        assertThat(users.getContent().size()).isGreaterThanOrEqualTo(1);
        users.getContent().forEach(userResponse -> {
            assertThat(userResponse.getEmail()).contains("aaa");
        });
    }

    @Test
    public void getUsersByDepartment() {
        UserRequest userRequest = new UserRequest();
        userRequest.setDepartment("Mobile Div");

        Page<UserResponse> users = userService.getUsers(PageRequest.of(0, 10), userRequest);

        assertThat(users.getContent().size()).isGreaterThanOrEqualTo(1);
        users.getContent().forEach(userResponse -> {
            assertThat(userResponse.getDepartment()).isEqualTo("Mobile Div");
        });
    }

}