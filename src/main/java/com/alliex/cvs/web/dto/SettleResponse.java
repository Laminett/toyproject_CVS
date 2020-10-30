package com.alliex.cvs.web.dto;

import com.alliex.cvs.domain.settle.Settle;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
public class SettleResponse {

    private Long id;

    private String username;

    private String fullName;

    private String aggregatedAt;

    public String getAggregatedAt() {
        return aggregatedAt.substring(0, 4) + "-" + aggregatedAt.substring(4, 6);
    }

    private Integer approvalCount;

    private Long approvalAmount;

    private Integer cancelCount;

    private Long cancelAmount;

    private Integer totalCount;

    private Long totalAmount;

    private String status;

    private boolean isApproved;

    private String adminId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifiedDate;

    @Override
    public String toString() {
        return "SettleResponse{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", fullName='" + fullName + '\'' +
                ", aggregatedAt='" + aggregatedAt + '\'' +
                ", approvalCount=" + approvalCount +
                ", approvalAmount=" + approvalAmount +
                ", cancelCount=" + cancelCount +
                ", cancelAmount=" + cancelAmount +
                ", totalCount=" + totalCount +
                ", totalAmount=" + totalAmount +
                ", status='" + status + '\'' +
                ", isApproved=" + isApproved +
                ", adminId='" + adminId + '\'' +
                ", createdDate=" + createdDate +
                ", modifiedDate=" + modifiedDate +
                '}';
    }

    @Builder
    public SettleResponse(Settle settle) {
        this.id = settle.getId();
        this.username = settle.getUser().getUsername();
        this.fullName = settle.getUser().getFullName();
        this.aggregatedAt = settle.getAggregatedAt();
        this.approvalCount = settle.getApprovalCount();
        this.approvalAmount = settle.getApprovalAmount();
        this.cancelCount = settle.getCancelCount();
        this.cancelAmount = settle.getApprovalAmount();
        this.totalCount = settle.getTotalCount();
        this.totalAmount = settle.getTotalAmount();
        this.status = settle.getStatus();
        this.adminId = settle.getAdminId();
        this.createdDate = settle.getCreatedDate();
        this.modifiedDate = settle.getModifiedDate();
    }

}