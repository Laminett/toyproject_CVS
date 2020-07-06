package com.alliex.cvs.web.dto;

import com.alliex.cvs.domain.point.Point;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PointRequest {

    private Long userId;

    private int point;

    @Builder
    public PointRequest(Long userId, int point) {
        this.userId=userId;
        this.point = point;
    }

    public Point toEntity() {
        return Point.builder()
                .userId(userId)
                .point(point)
                .build();
    }
}