package com.alliex.cvs.domain.type;

import lombok.Getter;

@Getter
public enum ProductPurchaseSearchType {

    PRODUCT_ID("product"),
    CATEGORY_ID("productCategory"),
    PURCHASE_DATE("purchaseDate");

    private final String field;

    ProductPurchaseSearchType(String field) {
        this.field = field;
    }

}
