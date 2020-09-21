package com.alliex.cvs.domain.type;

import lombok.Getter;

@Getter
public enum UserSearchType {

    USERNAME("username"),
    FULL_NAME("fullName"),
    EMAIL("email"),
    DEPARTMENT("department");

    private final String field;

    UserSearchType(String field) {
        this.field = field;
    }

}


