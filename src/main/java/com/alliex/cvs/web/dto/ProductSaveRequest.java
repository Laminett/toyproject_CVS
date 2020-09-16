package com.alliex.cvs.web.dto;

import com.alliex.cvs.domain.product.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class ProductSaveRequest {

    private String categoryId;

    private String barcode;

    private String name;

    private Long point;

    private Integer quantity;

    private Boolean isEnabled;

    private String createdId;


    @Builder
    public ProductSaveRequest(String categoryId, String barcode, String name, Long point, Integer quantity, Boolean isEnabled, String createdId) {
        this.categoryId = categoryId;
        this.barcode = barcode;
        this.name = name;
        this.point = point;
        this.quantity = quantity;
        this.isEnabled = isEnabled;
        this.createdId = createdId;
    }

    public Product toEntity() {
        return Product.builder()
                .categoryId(categoryId)
                .barcode(barcode)
                .name(name)
                .point(point)
                .quantity(quantity)
                .isEnabled(isEnabled)
                .createdId(createdId)
                .build();
    }

}
