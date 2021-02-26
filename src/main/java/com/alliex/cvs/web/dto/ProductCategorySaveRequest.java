package com.alliex.cvs.web.dto;

import com.alliex.cvs.entity.ProductCategory;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class ProductCategorySaveRequest {

    @NotBlank(message = "categoryName may not be blank.")
    @Size(max = 30, message = "categoryName must be 1-30 characters.")
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
                .adminId(adminId)
                .isEnabled(isEnabled)
                .build();
    }

}
