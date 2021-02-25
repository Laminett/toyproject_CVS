package com.alliex.cvs.web.dto;

import com.alliex.cvs.entity.ProductPurchase;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class ProductPurchaseUpdateRequest {

    private Integer purchaseQuantity;

    private Long purchaseAmount;

    private String adminId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate purchaseDate;

    public ProductPurchase toEntity() {
        return ProductPurchase.builder()
                .purchaseQuantity(purchaseQuantity)
                .purchaseAmount(purchaseAmount)
                .purchaseDate(purchaseDate)
                .adminId(adminId)
                .build();
    }

}
