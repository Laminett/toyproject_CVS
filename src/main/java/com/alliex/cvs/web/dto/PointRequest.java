package com.alliex.cvs.web.dto;

import com.alliex.cvs.domain.point.Point;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PointRequest {

    private Long id;

    private Double point;

    @Builder
    public PointRequest(Long id, Double point) {
        this.id=id;
        this.point = point;
    }

    public Point toEntity() {
        return Point.builder()
                .id(id)
                .point(point)
                .build();
    }
}