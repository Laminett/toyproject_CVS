package com.alliex.cvs.web.dto;

import com.alliex.cvs.domain.product.Product;
import com.alliex.cvs.domain.product.category.ProductCategory;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProductResponse {

    private Long id;

    private ProductCategory categoryId;

    private String barcode;

    private String name;

    private Long point;

    private Integer quantity;

    private Boolean isEnabled;

    private String adminId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifiedDate;

    public ProductResponse(Product entity) {
        this.id = entity.getId();
        this.categoryId = entity.getProductCategory();
        this.barcode = entity.getBarcode();
        this.name = entity.getName();
        this.point = entity.getPoint();
        this.quantity = entity.getQuantity();
        this.isEnabled = entity.getEnabled();
        this.adminId = entity.getAdminId();
        this.createdDate = entity.getCreatedDate();
        this.modifiedDate = entity.getModifiedDate();
    }

    public ProductResponse(String barcode, String name, Long point) {
        this.barcode = barcode;
        this.name = name;
        this.point = point;
    }
}
