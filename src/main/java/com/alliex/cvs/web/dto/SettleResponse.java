package com.alliex.cvs.web.dto;

import com.alliex.cvs.domain.settle.Settle;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class SettleResponse {

    private Long id;

    private String username;

    private String date;

    private Integer approvalCount;

    private Double approvalAmount;

    private Integer cancelCount;

    private Double cancelAmount;

    private Integer totalCount;

    private Double totalAmount;

    private String status;

    private String adminId;

    @Builder
    public SettleResponse(Settle settle) {
        this.id = settle.getId();
        this.username = settle.getUser().getUsername();
        this.date = settle.getDate();
        this.approvalCount = settle.getApprovalCount();
        this.approvalAmount = settle.getApprovalAmount();
        this.cancelCount = settle.getCancelCount();
        this.cancelAmount = settle.getApprovalAmount();
        this.totalCount = settle.getTotalCount();
        this.totalAmount = settle.getTotalAmount();
        this.status = settle.getStatus();
        this.adminId = settle.getAdminId();
    }
}
