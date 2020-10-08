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

    protected Boolean isEnabled;

    protected String modifiedId;

    public ProductCategory toEntity() {
        return ProductCategory.builder()
                .name(categoryName)
                .isEnabled(isEnabled)
                .modifiedId(modifiedId)
                .build();
    }
}
