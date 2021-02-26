package com.alliex.cvs.web.dto;

import com.alliex.cvs.entity.ProductCategory;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductCategorySaveRequest {

    private String categoryName;

    private String adminId;

    private Boolean isEnabled;

    @Builder
    public ProductCategorySaveRequest(String categoryName, String adminId, Boolean isEnabled) {
        this.categoryName = categoryName;
        this.adminId = adminId;
        this.isEnabled = isEnabled;
    }

    public ProductCategory toEntity() {
        return ProductCategory.builder()
                .name(categoryName.toUpperCase())
                .isEnabled(isEnabled)
                .adminId(adminId)
                .build();
    }

}
