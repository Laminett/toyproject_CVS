package com.alliex.cvs.web.dto;

import com.alliex.cvs.domain.product.category.ProductCategory;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;


@Getter
@ToString
public class ProductCategoryResponse {

    private Long id;

    private String name;

    private Boolean isEnabled;

    private String adminId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifiedDate;

    public ProductCategoryResponse(ProductCategory entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.isEnabled = entity.getEnabled();
        this.adminId = entity.getAdminId();
        this.createdDate = entity.getCreatedDate();
        this.modifiedDate = entity.getModifiedDate();
    }

}