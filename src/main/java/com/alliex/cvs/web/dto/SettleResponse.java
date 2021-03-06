package com.alliex.cvs.web.dto;

import com.alliex.cvs.entity.Settle;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
public class SettleResponse {

    private Long id;

    private String username;

    private String fullName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-yyyy")
    private LocalDate aggregatedAt;

    private Integer approvalCount;

    private Long approvalAmount;

    private Integer cancelCount;

    private Long cancelAmount;

    private Integer totalCount;

    private Long totalAmount;

    private String status;

    private boolean isApproved;

    private String adminId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Builder
    public SettleResponse(Settle settle) {
        this.id = settle.getId();
        this.username = settle.getUser().getUsername();
        this.fullName = settle.getUser().getFullName();
        this.aggregatedAt = settle.getAggregatedAt();
        this.approvalCount = settle.getApprovalCount();
        this.approvalAmount = settle.getApprovalAmount();
        this.cancelCount = settle.getCancelCount();
        this.cancelAmount = settle.getCancelAmount();
        this.totalCount = settle.getTotalCount();
        this.totalAmount = settle.getTotalAmount();
        this.status = settle.getStatus();
        this.adminId = settle.getAdminId();
        this.createdAt = settle.getCreatedAt();
        this.updatedAt = settle.getUpdatedAt();
    }

}