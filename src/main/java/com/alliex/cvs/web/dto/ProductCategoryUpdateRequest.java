package com.alliex.cvs.web.dto;

import com.alliex.cvs.entity.ProductCategory;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@NoArgsConstructor
@Validated
public class ProductCategoryUpdateRequest {

    protected String categoryName;

    protected String adminId;

    protected Boolean isEnabled;

    @Builder
    public ProductCategoryUpdateRequest(String categoryName, String adminId, Boolean isEnabled) {
        this.categoryName = categoryName;
        this.adminId = adminId;
        this.isEnabled = isEnabled;
    }

    public ProductCategory toEntity() {
        return ProductCategory.builder()
                .name(categoryName.toUpperCase())
                .adminId(adminId)
                .isEnabled(isEnabled)
                .build();
    }
}
