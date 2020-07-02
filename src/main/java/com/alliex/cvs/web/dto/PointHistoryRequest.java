package com.alliex.cvs.web.dto;

import com.alliex.cvs.domain.point.PointHistory;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PointHistoryRequest {

    private Long id;

    private Double point;

    private String registrant;

    @Builder
    public PointHistoryRequest(Long id, Double point, String registrant) {
        this.id = id;
        this.point = point;
        this.registrant = registrant;
    }

    public PointHistory toEntity() {
        return PointHistory.builder()
                .id(id)
                .point(point)
                .registrant(registrant)
                .build();
    }
}
