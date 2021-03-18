package com.alliex.cvs.web.dto;


import com.alliex.cvs.entity.Product;
import com.alliex.cvs.entity.ProductCategory;
import com.alliex.cvs.entity.ProductPurchase;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ProductPurchaseSaveRequest {

    private Long categoryId;

    private Long productId;

    private String adminId;

    @NotBlank(message = "purchaseQuantity may not be blank.")
    @Size(max = 5, message = "purchaseQuantity must be 1-5 digit.")
    private Integer purchaseQuantity;

    @NotBlank(message = "purchaseAmount may not be blank.")
    @Size(max = 17, message = "purchaseAmount must be 1-17 digit.")
    private Long purchaseAmount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate purchaseDate;

    @Builder
    public ProductPurchaseSaveRequest(Long categoryId, Long productId, Integer purchaseQuantity, Long purchaseAmount, LocalDate purchaseDate, String adminId, LocalDateTime createdDate) {
        this.categoryId = categoryId;
        this.productId = productId;
        this.adminId = adminId;
        this.purchaseQuantity = purchaseQuantity;
        this.purchaseAmount = purchaseAmount;
        this.purchaseDate = purchaseDate;
    }

    public ProductPurchase toEntity() {
        Product product = new Product();
        product.setId(productId);

        ProductCategory productCategory = new ProductCategory();
        productCategory.setId(categoryId);

        return ProductPurchase.builder()
                .categoryId(productCategory)
                .productId(product)
                .adminId(adminId)
                .purchaseQuantity(purchaseQuantity)
                .purchaseAmount(purchaseAmount)
                .purchaseDate(purchaseDate)
                .build();
    }

}
