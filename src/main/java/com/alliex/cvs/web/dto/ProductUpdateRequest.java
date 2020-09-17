package com.alliex.cvs.web.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ProductUpdateRequest {

    private Long id;

    private String categoryId;

    private String name;

    private Long point;

    private Integer quantity;

    private Boolean isEnabled;

    private String modifiedId;

    @Builder
    public ProductUpdateRequest(String categoryId, String name, Long point, Integer quantity, Boolean isEnabled, String modifiedId) {
        this.categoryId = categoryId;
        this.name = name;
        this.point = point;
        this.quantity = quantity;
        this.isEnabled = isEnabled;
        this.modifiedId = modifiedId;
    }

}
