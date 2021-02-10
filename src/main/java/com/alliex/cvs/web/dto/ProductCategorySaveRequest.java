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

    @Builder
    public ProductCategorySaveRequest(String categoryName, String adminId) {
        this.categoryName = categoryName;
        this.adminId = adminId;
    }

    public ProductCategory toEntity() {
        return ProductCategory.builder()
                .name(categoryName.toUpperCase())
                .adminId(adminId)
                .build();
    }

}
