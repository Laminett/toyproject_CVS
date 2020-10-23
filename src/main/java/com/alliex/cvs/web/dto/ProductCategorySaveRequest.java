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

    private String adminId;

    @Builder
    public ProductCategorySaveRequest(String categoryName, Boolean isEnabled, String adminId) {
        this.categoryName = categoryName;
        this.isEnabled = isEnabled;
        this.adminId = adminId;
    }

    public ProductCategory toEntity() {
        return ProductCategory.builder()
                .name(categoryName.toUpperCase())
                .isEnabled(isEnabled)
                .adminId(adminId)
                .build();
    }

}
