package com.alliex.cvs.web.dto;

import com.alliex.cvs.domain.user.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserResponse {

    private Long id;

    private String fullName;

    private String username;

    private String department;

    private String phoneNumber;

    private LocalDateTime createdDate;

    public UserResponse(User entity) {
        this.id = entity.getId();
        this.fullName = entity.getFullName();
        this.username = entity.getUsername();
        this.department = entity.getDepartment();
        this.phoneNumber = entity.getPhoneNumber();
        this.createdDate = entity.getCreatedDate();
    }

}
