package com.alliex.cvs.web.dto;

import com.alliex.cvs.entity.Product;
import com.alliex.cvs.entity.TransactionDetail;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TransactionDetailSaveRequest {

    private Integer quantity;

    private Long productId;

    private String requestId;

    @Builder
    public TransactionDetailSaveRequest(Integer quantity, Long productId, String requestId) {
        this.quantity = quantity;
        this.productId = productId;
        this.requestId = requestId;
    }

    public TransactionDetail toEntity() {
        Product setProductId = new Product();
        setProductId.setId(productId);

        return TransactionDetail.builder()
                .productQuantity(quantity)
                .product(setProductId)
                .requestId(requestId)
                .build();
    }

}
