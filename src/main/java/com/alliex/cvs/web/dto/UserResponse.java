package com.alliex.cvs.web.dto;

import com.alliex.cvs.domain.type.Role;
import com.alliex.cvs.domain.user.User;
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

    private Role role;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;

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
        this.role = entity.getRole();
        this.createdDate = entity.getCreatedDate();
    }

}
