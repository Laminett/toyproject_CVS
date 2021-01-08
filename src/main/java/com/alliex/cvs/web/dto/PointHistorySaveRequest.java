package com.alliex.cvs.web.dto;

import com.alliex.cvs.domain.point.PointHistory;
import com.alliex.cvs.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PointHistorySaveRequest {

    private Long userId;

    private Long point;

    @Builder
    public PointHistorySaveRequest(Long userId, Long point) {
        this.userId = userId;
        this.point = point;
    }

    public PointHistory toEntity() {
        User setUser = new User();
        setUser.setId(userId);

        return PointHistory.builder()
                .user(setUser)
                .point(point)
                .build();
    }

}