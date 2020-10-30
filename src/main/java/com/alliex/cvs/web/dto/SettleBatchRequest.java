package com.alliex.cvs.web.dto;

import com.alliex.cvs.domain.settle.Settle;
import com.alliex.cvs.domain.user.User;
import lombok.*;

@Data
@NoArgsConstructor
public class SettleBatchRequest {

    private Long userId;

    private String aggregatedAt;

    private Integer approvalCount;

    private Long approvalAmount;

    private Integer cancelCount;

    private Long cancelAmount;

    public Integer totalCount;

    public Long totalAmount;

    public Settle toEntity() {
        User setUser = new User();
        setUser.setId(userId);

        return Settle.builder()
                .user(setUser)
                .aggregatedAt(aggregatedAt)
                .approvalCount(approvalCount)
                .approvalAmount(approvalAmount)
                .cancelCount(cancelCount)
                .cancelAmount(cancelAmount)
                .totalCount(totalCount)
                .totalAmount(totalAmount)
                .build();
    }

}