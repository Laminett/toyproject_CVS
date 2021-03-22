package com.alliex.cvs.web.dto;

import com.alliex.cvs.entity.Product;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProductListResponse extends ProductResponse {

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public ProductListResponse(Product entity) {
        super(entity);

        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
    }

}
