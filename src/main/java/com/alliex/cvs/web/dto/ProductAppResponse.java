package com.alliex.cvs.web.dto;

import com.alliex.cvs.domain.product.Product;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class ProductAppResponse {

    private String name;

    private Long point;

    @Builder
    public ProductAppResponse(Product entity) {
        this.name = entity.getName();
        this.point = entity.getPoint();
    }

}
