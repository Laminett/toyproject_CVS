package com.alliex.cvs.web.dto;

import com.alliex.cvs.domain.product.Product;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class ProductResponse {

    private Long id;

    private String categoryId;

    private String barcode;

    private String name;

    private Long point;

    private Integer quantity;

    private Boolean isEnabled;

    private String createdId;

    private String modifiedId;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    public ProductResponse(Product entity) {
        this.id = entity.getId();
        this.categoryId = entity.getCategoryId();
        this.barcode = entity.getBarcode();
        this.name = entity.getName();
        this.point = entity.getPoint();
        this.quantity = entity.getQuantity();
        this.isEnabled = entity.getEnabled();
        this.createdId = entity.getCreatedId();
        this.createdDate = entity.getCreatedDate();
        this.modifiedId = entity.getModifiedId();
        this.modifiedDate = entity.getModifiedDate();
    }

}
