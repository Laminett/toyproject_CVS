package com.alliex.cvs.web.dto;

import com.alliex.cvs.domain.product.Product;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductRequest {

    private String searchField;

    private String searchValue;

    @Builder
    public ProductRequest(String searchField, String searchValue) {
        this.searchField = searchField;
        this.searchValue = searchValue;
    }

}
