package com.alliex.cvs.web.dto;

import com.alliex.cvs.entity.Product;
import com.alliex.cvs.entity.ProductCategory;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class ProductUpdateRequest {

    private Long categoryId;

    private String name;

    private Long point;

    private Integer quantity;

    private Boolean isEnabled;

    private String adminId;

    @Builder
    public ProductUpdateRequest(Long categoryId, String name, Long point, Integer quantity, Boolean isEnabled, String adminId) {
        this.categoryId = categoryId;
        this.name = name;
        this.point = point;
        this.quantity = quantity;
        this.isEnabled = isEnabled;
        this.adminId = adminId;
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
                .adminId(adminId)
                .build();
    }

}
