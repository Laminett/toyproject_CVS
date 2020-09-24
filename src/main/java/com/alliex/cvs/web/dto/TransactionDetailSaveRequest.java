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

    private Long transactionId;

    @Builder
    public TransactionDetailSaveRequest(Integer productQuantity, Long productId, TransactionState transactionState, Long transactionId) {
        this.productQuantity = productQuantity;
        this.productId = productId;
        this.transactionState = transactionState;
        this.transactionId = transactionId;
    }

    public TransactionDetail toEntity() {
        Product setProductId = new Product();
        setProductId.setId(productId);

        return TransactionDetail.builder()
                .productQuantity(productQuantity)
                .transactionId(transactionId)
                .transactionState(transactionState)
                .product(setProductId)
                .build();
    }

}
