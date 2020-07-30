package com.alliex.cvs.web.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ProductUpdateRequest {

    private Long id;

    private String categoryId;

    private String name;

    private Integer point;

    private Boolean isEnabled;

    private String modifiedId;

    @Builder
    public ProductUpdateRequest(String categoryId, String name, Integer point, Boolean isEnabled, String modifiedId) {
        this.categoryId = categoryId;
        this.name = name;
        this.point = point;
        this.isEnabled = isEnabled;
        this.modifiedId = modifiedId;
    }

}
