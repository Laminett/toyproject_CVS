package com.alliex.cvs.web.dto;

import com.alliex.cvs.domain.type.Role;
import com.alliex.cvs.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserSaveRequest {

    private String username;

    private String password;

    private String department;

    private String fullName;

    private String email;

    private String phoneNumber;

    private Role role;

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
