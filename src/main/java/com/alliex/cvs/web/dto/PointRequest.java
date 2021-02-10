package com.alliex.cvs.web.dto;

import com.alliex.cvs.entity.Point;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PointRequest {

    private Long userId;

    private Long point;

    @Builder
    public PointRequest(Long userId, Long point) {
        this.userId = userId;
        this.point = point;
    }

    public Point toEntity() {
        return Point.builder()
                .userId(userId)
                .point(point)
                .build();
    }
}