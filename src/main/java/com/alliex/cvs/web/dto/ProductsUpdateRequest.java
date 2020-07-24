package com.alliex.cvs.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ProductsUpdateRequest {

    private Long id;

    private String categoryId;

    private String name;

    private int point;

    private boolean getisEnabled;

    private String modifiedId;

    @Builder
    public ProductsUpdateRequest(String categoryId, String name, int point, boolean getisEnabled, String modifiedId) {
        this.categoryId = categoryId;
        this.name = name;
        this.point = point;
        this.getisEnabled = getisEnabled;
        this.modifiedId = modifiedId;
    }
}
