package com.alliex.cvs.web.dto;

import com.alliex.cvs.entity.ProductCategory;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;


@Getter
@ToString
public class ProductCategoryResponse {

    private Long id;

    private String name;

    private String adminId;

    private Boolean isEnabled;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    public ProductCategoryResponse(ProductCategory entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.adminId = entity.getAdminId();
        this.isEnabled = entity.getIsEnabled();
        this.createdDate = entity.getCreatedDate();
        this.modifiedDate = entity.getModifiedDate();
    }

}
