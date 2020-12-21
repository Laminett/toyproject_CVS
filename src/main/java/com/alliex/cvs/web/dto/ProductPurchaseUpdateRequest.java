package com.alliex.cvs.web.dto;

import com.alliex.cvs.domain.product.purchase.ProductPurchase;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class ProductPurchaseUpdateRequest {

    Integer purchaseQuantity;

    Long purchaseAmount;

    String adminId;

    LocalDate purchaseDate;

    public ProductPurchase toEntity() {
        return ProductPurchase.builder()
                .purchaseQuantity(purchaseQuantity)
                .purchaseAmount(purchaseAmount)
                .purchaseDate(purchaseDate)
                .adminId(adminId)
                .build();
    }
}
