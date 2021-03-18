package com.alliex.cvs.web.dto;

import com.alliex.cvs.domain.type.PointHistoryStatus;
import com.alliex.cvs.entity.PointHistory;
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

    private Long point;

    private PointHistoryStatus status;

    private String adminId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Builder
    public PointHistoryResponse(PointHistory entity) {
        this.id = entity.getId();
        this.username = entity.getUser().getUsername();
        this.fullName = entity.getUser().getFullName();
        this.point = entity.getPoint();
        this.status = entity.getStatus();
        this.adminId = entity.getAdminId();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
    }

}