package com.alliex.cvs.entity;

import com.alliex.cvs.domain.BaseTimeEntity;
import com.alliex.cvs.entity.Product;
import com.alliex.cvs.entity.ProductCategory;
import com.alliex.cvs.web.dto.ProductPurchaseUpdateRequest;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ProductPurchase extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_product_purchase_product_category_id"))
    private ProductCategory productCategory;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_product_purchase_product_id"))
    private Product product;

    @Column(nullable = false)
    private Integer purchaseQuantity;

    @Column(nullable = false)
    private Long purchaseAmount;

    private String adminId;

    @Column(nullable = false)
    private LocalDate purchaseDate;

    @Builder
    public ProductPurchase(Long id, ProductCategory categoryId, Product productId, Integer purchaseQuantity, Long purchaseAmount, String adminId, LocalDate purchaseDate) {
        this.id = id;
        this.productCategory = categoryId;
        this.product = productId;
        this.purchaseQuantity = purchaseQuantity;
        this.purchaseAmount = purchaseAmount;
        this.adminId = adminId;
        this.purchaseDate = purchaseDate;
    }

    public void update(ProductPurchaseUpdateRequest productPurchaseUpdateRequest) {
        this.purchaseQuantity = productPurchaseUpdateRequest.getPurchaseQuantity();
        this.purchaseAmount = productPurchaseUpdateRequest.getPurchaseAmount();
        this.adminId = productPurchaseUpdateRequest.getAdminId();
        this.purchaseDate = productPurchaseUpdateRequest.getPurchaseDate();
    }

}
