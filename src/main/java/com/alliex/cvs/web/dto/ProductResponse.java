package com.alliex.cvs.web.dto;

import com.alliex.cvs.entity.Product;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProductResponse {

    private Long id;

    private Long categoryId;

    private String categoryName;

    private String barcode;

    private String name;

    private Long point;

    private Integer quantity;

    private Boolean isEnabled;

    private String adminId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

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
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
    }

}
