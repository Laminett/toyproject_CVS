package com.alliex.cvs.web.dto;

import com.alliex.cvs.domain.product.Product;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductAppResponse {

    private String name;

    private Integer point;

    @Builder
    public ProductAppResponse(Product entity) {
        this.name = entity.getName();
        this.point = entity.getPoint();
    }

}
