package com.alliex.cvs.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SettleTransMonthlySumRequest {

    private Long userId;

    private Long approvalCount;

    private Long approvalAmount;

    private Long cancelCount;

    private Long cancelAmount;

    public Long getTotalCount() {
        return approvalCount + cancelCount;
    }

    public Long getTotalAmount() {
        return approvalAmount - cancelAmount;
    }

}