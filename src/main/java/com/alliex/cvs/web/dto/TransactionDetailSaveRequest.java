package com.alliex.cvs.web.dto;

import com.alliex.cvs.domain.product.Product;
import com.alliex.cvs.domain.type.TransactionState;
import com.alliex.cvs.domain.transactionDetail.TransactionDetail;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionDetailSaveRequest {

    private Integer productQuantity;

    private Long productId;

    private TransactionState transactionState;

    private String requestId;

    @Builder
    public TransactionDetailSaveRequest(Integer productQuantity, Long productId, TransactionState transactionState, String requestId) {
        this.productQuantity = productQuantity;
        this.productId = productId;
        this.transactionState = transactionState;
        this.requestId = requestId;
    }

    public TransactionDetail toEntity() {
        Product setProductId = new Product();
        setProductId.setId(productId);

        return TransactionDetail.builder()
                .productQuantity(productQuantity)
                .requestId(requestId)
                .transactionState(transactionState)
                .product(setProductId)
                .build();
    }

}
