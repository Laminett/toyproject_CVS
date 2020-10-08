package com.alliex.cvs.web.dto;

import com.alliex.cvs.domain.product.Product;
import com.alliex.cvs.domain.product.category.ProductCategory;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class ProductSaveRequest {

    private Long categoryId;

    private String categoryName;

    private String barcode;

    private String name;

    private Long point;

    private Integer quantity;

    private Boolean isEnabled;

    private String createdId;


    @Builder
    public ProductSaveRequest(Long categoryId, String categoryName, String barcode, String name, Long point, Integer quantity, Boolean isEnabled, String createdId) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.barcode = barcode;
        this.name = name;
        this.point = point;
        this.quantity = quantity;
        this.isEnabled = isEnabled;
        this.createdId = createdId;
    }

    public Product toEntity() {
        ProductCategory setCategoryId = new ProductCategory();
        setCategoryId.setId(categoryId);

        return Product.builder()
                .productCategory(setCategoryId)
                .barcode(barcode)
                .name(name)
                .point(point)
                .quantity(quantity)
                .isEnabled(isEnabled)
                .createdId(createdId)
                .build();
    }

}
