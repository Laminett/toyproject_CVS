package com.alliex.cvs.web.dto;

import com.alliex.cvs.domain.type.Role;
import com.alliex.cvs.domain.type.UserStatus;
import com.alliex.cvs.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserUpdateRequest {

    private String password;

    private String department;

    private String fullName;

    private String email;

    private String phoneNumber;

    private Role role;

    private UserStatus status;

    public User toEntity() {
        return User.builder()
                .password(password)
                .department(department)
                .fullName(fullName)
                .email(email)
                .phoneNumber(phoneNumber)
                .role(role)
                .status(status)
                .build();
    }

}
