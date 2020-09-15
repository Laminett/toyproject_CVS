package com.alliex.cvs.web.dto;

import com.alliex.cvs.domain.settle.Settle;
import com.alliex.cvs.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SettleSaveRequest {

    private Long userId;

    private Integer approvalCount;

    private Double approvalAmount;

    private Integer cancelCount;

    private Double cancelAmount;

    @Builder
    public SettleSaveRequest(Long userId, Integer approvalCount) {
        this.userId = userId;
        this.approvalCount = approvalCount;
    }

    public Settle toEntity() {
        User setUser = new User();
        setUser.setId(userId);

        return Settle.builder()
                .user(setUser)
                .approvalCount(approvalCount)
                .build();
    }

}