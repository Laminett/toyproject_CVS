package com.alliex.cvs.web.dto;

import com.alliex.cvs.domain.type.PointHistoryStatus;
import com.alliex.cvs.entity.PointHistory;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PointHistoryUpdateRequest {

    private Long id;

    private PointHistoryStatus status;

    private String adminId;

    @Builder
    public PointHistoryUpdateRequest(Long id, PointHistoryStatus status, String adminId) {
        this.id = id;
        this.status = status;
        this.adminId = adminId;
    }

    public PointHistory toEntity() {
        return PointHistory.builder()
                .id(id)
                .status(status)
                .adminId(adminId)
                .build();
    }

}