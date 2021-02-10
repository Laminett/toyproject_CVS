package com.alliex.cvs.web.dto;

import com.alliex.cvs.entity.Product;
import lombok.Builder;
import lombok.Getter;

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
