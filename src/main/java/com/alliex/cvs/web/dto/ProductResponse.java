package com.alliex.cvs.web.dto;

import com.alliex.cvs.entity.Product;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProductResponse {

    private Long id;

    private String categoryName;

    private Long categoryId;

    private String barcode;

    private String name;

    private Long point;

    private Integer quantity;

    private Boolean isEnabled;

    private String adminId;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    public ProductResponse(Product entity) {
        this.id = entity.getId();
        this.categoryId = entity.getProductCategory().getId();
        this.categoryName = entity.getProductCategory().getName();
        this.barcode = entity.getBarcode();
        this.name = entity.getName();
        this.point = entity.getPoint();
        this.quantity = entity.getQuantity();
        this.isEnabled = entity.getEnabled();
        this.adminId = entity.getAdminId();
        this.createdDate = entity.getCreatedDate();
        this.modifiedDate = entity.getModifiedDate();
    }

}
