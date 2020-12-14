package com.alliex.cvs.web.dto;


import com.alliex.cvs.domain.product.Product;
import com.alliex.cvs.domain.product.category.ProductCategory;
import com.alliex.cvs.domain.product.purchase.ProductPurchase;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ProductPurchaseSaveRequest {

    Long categoryId;

    Long productId;

    String adminId;

    Integer purchaseQuantity;

    Long purchaseAmount;

    LocalDate purchaseDate;

    LocalDateTime createdDate;

    @Builder
    public ProductPurchaseSaveRequest(Long categoryId, Long productId, Integer purchaseQuantity, Long purchaseAmount, LocalDate purchaseDate, String adminId, LocalDateTime createdDate) {
        this.categoryId = categoryId;
        this.productId = productId;
        this.purchaseQuantity = purchaseQuantity;
        this.purchaseAmount = purchaseAmount;
        this.purchaseDate = purchaseDate;
        this.adminId = adminId;
        this.createdDate = createdDate;
    }

    public ProductPurchase toEntity() {
        Product product = new Product();
        product.setId(productId);

        ProductCategory productCategory = new ProductCategory();
        productCategory.setId(categoryId);

        return ProductPurchase.builder()
                .categoryId(productCategory)
                .productId(product)
                .purchaseQuantity(purchaseQuantity)
                .purchaseAmount(purchaseAmount)
                .purchaseDate(purchaseDate)
                .adminId(adminId)
                .build();
    }

}
