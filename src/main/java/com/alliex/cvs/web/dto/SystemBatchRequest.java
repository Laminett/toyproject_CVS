package com.alliex.cvs.web.dto;

import com.alliex.cvs.domain.settle.Settle;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SystemBatchRequest {

    private String aggregatedAt;

    @Builder
    public SystemBatchRequest(String aggregatedAt) {
        this.aggregatedAt = aggregatedAt;
    }

}