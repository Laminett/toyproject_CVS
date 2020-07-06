package com.alliex.cvs.web.dto;

import com.alliex.cvs.domain.point.PointHistory;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PointHistoryRequest {

    private Long userId;

    private int point;

    private String registrant;

    @Builder
    public PointHistoryRequest(Long userId, Integer point, String registrant) {
        this.userId = userId;
        this.point = point;
        this.registrant = registrant;
    }

    public PointHistory toEntity() {
        return PointHistory.builder()
                .userId(userId)
                .point(point)
                .registrant(registrant)
                .build();
    }
}
