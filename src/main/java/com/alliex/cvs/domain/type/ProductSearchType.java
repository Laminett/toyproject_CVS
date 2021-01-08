package com.alliex.cvs.domain.type;

import lombok.Getter;

@Getter
public enum ProductSearchType {

    CATEGORY_NAME("productCategory"),
    NAME("name"),
    IS_ENABLED("isEnabled");

    private final String value;

    ProductSearchType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
