package com.alliex.cvs.web.dto;

import com.alliex.cvs.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserUpdateRequest {

    protected String password;

    protected String department;

    protected String fullName;

    protected String email;

    protected String phoneNumber;

    public User toEntity() {
        return User.builder()
                .password(password)
                .department(department)
                .fullName(fullName)
                .email(email)
                .phoneNumber(phoneNumber)
                .build();
    }

}
