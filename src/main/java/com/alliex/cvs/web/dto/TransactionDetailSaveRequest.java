package com.alliex.cvs.web.dto;

import com.alliex.cvs.domain.product.Product;
import com.alliex.cvs.domain.type.TransState;
import com.alliex.cvs.domain.transactionDetail.TransactionDetail;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionDetailSaveRequest {

    private Integer productAmount;

    private Long productId;

    private TransState transState;

    private Long transId;

    @Builder
    public TransactionDetailSaveRequest(Integer productAmount, Long productId, TransState transState, Long transId) {
        this.productAmount = productAmount;
        this.productId = productId;
        this.transState = transState;
        this.transId = transId;
    }

    public TransactionDetail toEntity() {
        Product setProductId = new Product();
        setProductId.setId(productId);

        return TransactionDetail.builder()
                .productAmount(productAmount)
                .transId(transId)
                .transState(transState)
                .product(setProductId)
                .build();
    }

}
