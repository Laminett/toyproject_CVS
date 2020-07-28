package com.alliex.cvs.web.dto;

import com.alliex.cvs.domain.product.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductsSaveRequest {

    private String categoryId;

    private String barcode;

    private String name;

    private Integer point;

    private boolean getisEnabled;

    private String createdId;


    @Builder
    public ProductsSaveRequest(String categoryId, String barcode, String name, Integer point, boolean getisEnabled, String createdId) {
        this.categoryId = categoryId;
        this.barcode = barcode;
        this.name = name;
        this.point = point;
        this.getisEnabled = getisEnabled;
        this.createdId = createdId;
    }

    public Product toEntity() {
        return Product.builder()
                .categoryId(categoryId)
                .barcode(barcode)
                .name(name)
                .point(point)
                .isEnabled(getisEnabled)
                .createdId(createdId)
                .build();
    }

}
