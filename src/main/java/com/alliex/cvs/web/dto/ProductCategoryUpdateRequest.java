package com.alliex.cvs.web.dto;

import com.alliex.cvs.domain.product.category.ProductCategory;
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

    public ProductCategory toEntity() {
        return ProductCategory.builder()
                .name(categoryName.toUpperCase())
                .adminId(adminId)
                .build();
    }
}
