package com.alliex.cvs.web.dto;

import com.alliex.cvs.domain.type.Role;
import com.alliex.cvs.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class UserSaveRequest {

    @NotBlank(message = "username may not be blank.")
    @Size(min = 5, max = 20, message = "username must be 5-20 characters.")
    @Pattern(regexp = "[a-zA-z0-9]+")
    private String username;

    @NotBlank(message = "password may not be blank.")
    @Size(min = 5, max = 20, message = "password must be 5-20 characters.")
    private String password;

    private String department;

    @NotBlank(message = "fullName may not be blank.")
    @Size(max = 20, message = "fullName must be 1-20 characters.")
    private String fullName;

    @NotBlank(message = "email may not be blank.")
    @Size(max = 20, message = "email must be 1-20 characters.")
    private String email;

    @NotBlank(message = "phoneNumber may not be blank.")
    @Size(max = 20, message = "phoneNumber must be 1-20 characters.")
    private String phoneNumber;

    private Role role = Role.USER;

    @Builder
    public UserSaveRequest(String username, String password, String department, String fullName, String email, String phoneNumber, Role role) {
        this.username = username;
        this.password = password;
        this.department = department;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }

    public User toEntity() {
        return User.builder()
                .username(username)
                .password(password)
                .department(department)
                .fullName(fullName)
                .email(email)
                .phoneNumber(phoneNumber)
                .role(role)
                .build();
    }

}
