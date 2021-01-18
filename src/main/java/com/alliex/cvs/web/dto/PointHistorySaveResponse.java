package com.alliex.cvs.web.dto;

import lombok.Data;

@Data
public class PointHistorySaveResponse {

    private Long userId;

    private Long point;

    public PointHistorySaveResponse(Long userId, Long point) {
        this.userId = userId;
        this.point = point;
    }

}