package com.alliex.cvs.web.dto;

import com.alliex.cvs.domain.type.TransState;
import com.alliex.cvs.domain.type.TransType;
import com.alliex.cvs.domain.transaction.Transaction;
import com.alliex.cvs.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class TransactionSaveRequest {

    private Long userId;

    private Long merchantId;

    private Long originid;

    private Integer point;

    private TransState state;

    private TransType type;

    private String requestid;

    private List<Map<String, String>> transProduct;

    @Builder
    public TransactionSaveRequest(Long userId, Long merchantId, Long originid, Integer point, TransState state, TransType type, String requestid, List<Map<String, String>> transProduct) {
        this.userId = userId;
        this.merchantId = merchantId;
        this.originid = originid;
        this.point = point;
        this.state = state;
        this.type = type;
        this.requestid = requestid;
        this.transProduct = transProduct;
    }

    @Builder
    public TransactionSaveRequest(Long userId, Long merchantId, Long originid, Integer point, TransState state, TransType type, String requestid) {
        this.userId = userId;
        this.merchantId = merchantId;
        this.originid = originid;
        this.point = point;
        this.state = state;
        this.type = type;
        this.requestid = requestid;
    }

    public Transaction toEntity() {
        User setUserId = new User();
        setUserId.setId(userId);

        return Transaction.builder()
                .user(setUserId)
                .merchantId(merchantId)
                .originid(originid)
                .point(point)
                .state(state)
                .type(type)
                .requestid(requestid)
                .build();
    }

}
