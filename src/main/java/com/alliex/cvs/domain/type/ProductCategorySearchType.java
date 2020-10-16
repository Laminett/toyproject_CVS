package com.alliex.cvs.domain.type;

import lombok.Getter;

@Getter
public enum ProductCategorySearchType {

    NAME("name"),
    IS_ENABLED("isEnabled");

    private final String field;

    ProductCategorySearchType(String field) {
        this.field = field;
    }
}
