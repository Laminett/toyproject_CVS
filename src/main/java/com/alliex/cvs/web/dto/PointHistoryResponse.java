package com.alliex.cvs.web.dto;

import com.alliex.cvs.domain.point.PointHistory;
import com.alliex.cvs.domain.user.User;
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

    private User user;

    private Integer point;

    private String status;

    private String adminId;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    private String isApproved;

    private String requestDate;

    private String updateDate;

    @Builder
    public PointHistoryResponse(PointHistory entity) {
        this.id = entity.getId();
        this.user = entity.getUser();
        this.point = entity.getPoint();
        this.status = entity.getStatus();
        this.adminId = entity.getAdminId();
        this.createdDate = entity.getCreatedDate();
        this.modifiedDate = entity.getModifiedDate();
    }

}