package com.alliex.cvs.web.dto;

import com.alliex.cvs.domain.product.Product;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductsAppResponse {

    private String name;

    private Integer point;

    @Builder
    public ProductsAppResponse(Product entity) {
        this.name = entity.getName();
        this.point = entity.getPoint();
    }
}
