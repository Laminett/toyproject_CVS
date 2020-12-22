package com.alliex.cvs.web.dto;


import com.alliex.cvs.domain.product.Product;
import com.alliex.cvs.domain.product.category.ProductCategory;
import com.alliex.cvs.domain.product.purchase.ProductPurchase;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ProductPurchaseSaveRequest {

    private Long categoryId;

    private Long productId;

    private String adminId;

    private Integer purchaseQuantity;

    private Long purchaseAmount;

    private LocalDate purchaseDate;

    private LocalDateTime createdDate;

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
