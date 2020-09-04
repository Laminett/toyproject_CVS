package com.alliex.cvs.web.dto;

import com.alliex.cvs.domain.product.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductSaveRequest {

    private String categoryId;

    private String barcode;

    private String name;

    private Integer point;

    private Integer amount;

    private Boolean isEnabled;

    private String createdId;


    @Builder
    public ProductSaveRequest(String categoryId, String barcode, String name, Integer point, Integer amount, Boolean isEnabled, String createdId) {
        this.categoryId = categoryId;
        this.barcode = barcode;
        this.name = name;
        this.point = point;
        this.amount = amount;
        this.isEnabled = isEnabled;
        this.createdId = createdId;
    }

    public Product toEntity() {
        return Product.builder()
                .categoryId(categoryId)
                .barcode(barcode)
                .name(name)
                .point(point)
                .amount(amount)
                .isEnabled(isEnabled)
                .createdId(createdId)
                .build();
    }

}
