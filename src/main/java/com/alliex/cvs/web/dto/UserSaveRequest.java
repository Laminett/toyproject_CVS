package com.alliex.cvs.web.dto;

import com.alliex.cvs.domain.type.Role;
import com.alliex.cvs.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserSaveRequest {

    protected String username;

    protected String password;

    protected String department;

    protected String fullName;

    protected String email;

    protected String phoneNumber;

    @Builder
    public UserSaveRequest(String username, String password, String department, String fullName, String email, String phoneNumber) {
        this.username = username;
        this.password = password;
        this.department = department;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public User toEntity() {
        return User.builder()
                .username(username)
                .password(password)
                .department(department)
                .fullName(fullName)
                .email(email)
                .phoneNumber(phoneNumber)
                .role(Role.USER)
                .build();
    }

}
