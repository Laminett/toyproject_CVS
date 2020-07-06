package com.alliex.cvs.web.dto;

import com.alliex.cvs.domain.point.PointHistory;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class PointHistoryResponse {

    private Long id;

    private Long userId;

    private int point;

    private String registrant;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDated;

    @Builder
    public PointHistoryResponse(PointHistory entity) {
        this.id = entity.getId();
        this.userId = entity.getUserId();
        this.point = entity.getPoint();
        this.registrant = entity.getRegistrant();
        this.createdDate = entity.getCreatedDate();
        this.modifiedDated = entity.getModifiedDate();
    }

}