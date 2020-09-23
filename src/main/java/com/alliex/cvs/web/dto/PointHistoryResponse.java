package com.alliex.cvs.web.dto;

import com.alliex.cvs.domain.point.PointHistory;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
public class PointHistoryResponse {

    private Long id;

    private String username;

    private String fullName;

    private Integer point;

    private String status;

    private String adminId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifiedDate;

    @Builder
    public PointHistoryResponse(PointHistory entity) {
        this.id = entity.getId();
        this.username = entity.getUser().getUsername();
        this.fullName = entity.getUser().getFullName();
        this.point = entity.getPoint();
        this.status = entity.getStatus();
        this.adminId = entity.getAdminId();
        this.createdDate = entity.getCreatedDate();
        this.modifiedDate = entity.getModifiedDate();
    }

}