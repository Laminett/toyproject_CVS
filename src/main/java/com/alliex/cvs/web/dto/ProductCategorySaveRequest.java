package com.alliex.cvs.web.dto;

import com.alliex.cvs.domain.product.category.ProductCategory;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductCategorySaveRequest {

    private String categoryName;

    private Boolean isEnabled;

    private String createdId;

    @Builder
    public ProductCategorySaveRequest(String categoryName, Boolean isEnabled, String createdId) {
        this.categoryName = categoryName;
        this.isEnabled = isEnabled;
        this.createdId = createdId;
    }

    public ProductCategory toEntity() {
        return ProductCategory.builder()
                .name(categoryName)
                .isEnabled(isEnabled)
                .createdId(createdId)
                .build();
    }

}
