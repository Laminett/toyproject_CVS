package com.alliex.cvs.web.dto;

import com.alliex.cvs.entity.Transaction;
import com.alliex.cvs.domain.type.TransactionState;
import com.alliex.cvs.domain.type.TransactionType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class TransactionRefundResponse {

    private TransactionState state;

    private String originRequestId;

    private TransactionType type;

    private String requestId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;

    public TransactionRefundResponse(Transaction entity) {
        this.state = entity.getState();
        this.originRequestId = entity.getOriginRequestId();
        this.type = entity.getType();
        this.requestId = entity.getRequestId();
        this.createdDate = entity.getCreatedDate();
    }
}
