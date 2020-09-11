package com.alliex.cvs.web.dto;

import com.alliex.cvs.domain.user.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserResponse {

    private Long id;

    private String username;

    private String department;

    private String fullName;

    private String email;

    private String phoneNumber;

    private LocalDateTime createdDate;

    public UserResponse(User entity) {
        this.id = entity.getId();
        this.username = entity.getUsername();
        this.department = entity.getDepartment();
        this.fullName = entity.getFullName();
        this.email = entity.getEmail();
        this.phoneNumber = entity.getPhoneNumber();
        this.createdDate = entity.getCreatedDate();
    }

}
