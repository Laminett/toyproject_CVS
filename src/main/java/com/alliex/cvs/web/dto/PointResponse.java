package com.alliex.cvs.web.dto;

import com.alliex.cvs.domain.point.Point;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PointResponse {

    private Long id;

    private Long userId;

    private Long point;

    @Builder
    public PointResponse(Point entity) {
        this.id = entity.getId();
        this.userId = entity.getUser().getId();
        this.point = entity.getPoint();
    }

}