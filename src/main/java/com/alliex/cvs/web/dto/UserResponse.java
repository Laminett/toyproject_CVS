package com.alliex.cvs.web.dto;

import com.alliex.cvs.domain.type.Role;
import com.alliex.cvs.domain.type.UserStatus;
import com.alliex.cvs.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class UserResponse {

    private Long id;

    private String username;

    private String department;

    private String fullName;

    private String email;

    private String phoneNumber;

    private UserStatus status;

    private Role role;

    private Long point;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;

    public UserResponse(String username) {
        this.username = username;
    }

    public UserResponse(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    public UserResponse(User entity) {
        this.id = entity.getId();
        this.username = entity.getUsername();
        this.department = entity.getDepartment();
        this.fullName = entity.getFullName();
        this.email = entity.getEmail();
        this.phoneNumber = entity.getPhoneNumber();
        this.status = entity.getStatus();
        this.role = entity.getRole();
        this.createdDate = entity.getCreatedDate();

        if (entity.getPoint() != null) {
            this.point = entity.getPoint().getPoint();
        }
    }

}
