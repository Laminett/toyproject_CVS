package com.alliex.cvs.web.dto;

import com.alliex.cvs.entity.Settle;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SettleUpdateRequest {

    private Long id;

    private String status;

    private String adminId;

    @Builder
    public SettleUpdateRequest(Long id, String status, String adminId) {
        this.id = id;
        this.status = status;
        this.adminId = adminId;
    }

    public Settle toEntity() {
        return Settle.builder()
                .id(id)
                .status(status)
                .adminId(adminId)
                .build();
    }

}