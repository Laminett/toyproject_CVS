package com.alliex.cvs.web.dto;

import com.alliex.cvs.domain.point.PointHistory;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PointHistoryResponse {

    private Long id;

    private Double point;

    private String registrant;

    @Builder
    public PointHistoryResponse(PointHistory entity) {
        this.id = entity.getId();
        this.point = entity.getPoint();
        this.registrant = entity.getRegistrant();
    }

}
