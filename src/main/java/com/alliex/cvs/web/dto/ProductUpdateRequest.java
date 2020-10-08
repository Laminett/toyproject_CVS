package com.alliex.cvs.web.dto;

import com.alliex.cvs.domain.product.Product;
import com.alliex.cvs.domain.product.category.ProductCategory;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ProductUpdateRequest {

    private Long id;

    private String categoryName;

    private Long categoryId;

    private String name;

    private Long point;

    private Integer quantity;

    private Boolean isEnabled;

    private String modifiedId;

    @Builder
    public ProductUpdateRequest(Long categoryId, String categoryName, String name, Long point, Integer quantity, Boolean isEnabled, String modifiedId) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.name = name;
        this.point = point;
        this.quantity = quantity;
        this.isEnabled = isEnabled;
        this.modifiedId = modifiedId;
    }

    public Product toEntity() {
        ProductCategory setCategoryId = new ProductCategory();
        setCategoryId.setId(categoryId);

        return Product.builder()
                .productCategory(setCategoryId)
                .name(name)
                .point(point)
                .quantity(quantity)
                .isEnabled(isEnabled)
                .modifiedId(modifiedId)
                .build();
    }

}
