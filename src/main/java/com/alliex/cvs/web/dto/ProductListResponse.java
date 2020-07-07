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
    private String price;
    private String useyn;
    private String created_id;
    private LocalDateTime created_date;
    private String modified_id;
    private LocalDateTime modified_date;

    public ProductListResponse(Product entity){
        this.id=entity.getId();
        this.categoryid=entity.getCategoryid();
        this.barcode=entity.getBarcode();
        this.name=entity.getName();
        this.price=entity.getPrice();
        this.useyn=entity.getUseyn();
        this.created_id=entity.getCreated_id();
        this.created_date=entity.getCreatedDate();
        this.modified_id=entity.getModified_id();
        this.modified_date=entity.getModifiedDate();
    }


}
