package com.alliex.cvs.web.dto;

import com.alliex.cvs.entity.Product;
import com.alliex.cvs.entity.ProductPurchase;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@ToString
public class ProductPurchaseResponse {

    private Long id;

    private Product product;

    private String adminId;

    private Long purchaseAmount;

    private Integer purchaseQuantity;

    private LocalDate purchaseDate;

    private LocalDateTime createdAt;

    public ProductPurchaseResponse(ProductPurchase entity) {
        this.id = entity.getId();
        this.product = entity.getProduct();
        this.adminId = entity.getAdminId();
        this.purchaseAmount = entity.getPurchaseAmount();
        this.purchaseQuantity = entity.getPurchaseQuantity();
        this.createdAt = entity.getCreatedAt();
        this.purchaseDate = entity.getPurchaseDate();
    }

}
