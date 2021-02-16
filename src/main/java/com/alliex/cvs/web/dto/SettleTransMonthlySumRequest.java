package com.alliex.cvs.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SettleTransMonthlySumRequest {

    private Long userId;

    private Integer approvalCount;

    private Long approvalAmount;

    private Integer cancelCount;

    private Long cancelAmount;

    public Integer getTotalCount() {
        return approvalCount + cancelCount;
    }

    public Long getTotalAmount() {
        return approvalAmount - cancelAmount;
    }

}