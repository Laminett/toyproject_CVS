package com.alliex.cvs.web.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class TransactionRequest {

    private String searchField;

    private String searchValue;

    @Builder
    public TransactionRequest(String searchField, String searchValue) {
        this.searchField = searchField;
        this.searchValue = searchValue;
    }

}
