package com.alliex.cvs.web.dto;

import com.alliex.cvs.domain.product.Product;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class ProductListResponse {

    private String id;

    private String categoryid;

    private String barcode;

    private String name;

    private Integer point;

    private Boolean isEnabled;

    private String createdId;

    private LocalDateTime createdDate;

    private String modifiedId;

    private LocalDateTime modifiedDate;

    public ProductListResponse(Product entity){
        this.id = entity.getId();
        this.categoryid = entity.getCategoryId();
        this.barcode = entity.getBarcode();
        this.name = entity.getName();
        this.point = entity.getPoint();
        this.isEnabled = entity.getIsEnabled();
        this.createdId = entity.getCreatedId();
        this.createdDate = entity.getCreatedDate();
        this.modifiedId = entity.getModifiedId();
        this.modifiedDate = entity.getModifiedDate();
    }

}
