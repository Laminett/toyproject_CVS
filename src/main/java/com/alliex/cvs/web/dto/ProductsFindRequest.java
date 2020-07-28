package com.alliex.cvs.web.dto;

import com.alliex.cvs.domain.product.Product;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductsFindRequest {

    private String searchSelect;

    private String searchText;

    @Builder
    public ProductsFindRequest(String searchSelect, String searchText) {
        this.searchSelect = searchSelect;
        this.searchText = searchText;
    }
}
