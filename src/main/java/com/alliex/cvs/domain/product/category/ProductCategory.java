package com.alliex.cvs.domain.product.category;

import com.alliex.cvs.domain.BaseTimeEntity;
import com.alliex.cvs.domain.product.Product;
import com.alliex.cvs.domain.product.purchase.ProductPurchase;
import com.alliex.cvs.web.dto.ProductCategoryUpdateRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(indexes = {
        @Index(name = "uix_name", columnList = "name", unique = true)
})
public class ProductCategory extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String adminId;

    @OneToMany(mappedBy = "productCategory", cascade = CascadeType.ALL)
    @JsonIgnore
    private Collection<Product> productId;

    @OneToMany(mappedBy = "productCategory", cascade = CascadeType.ALL)
    @JsonIgnore
    private Collection<ProductPurchase> productPurchaseId;

    @Builder
    public ProductCategory(Long id, String name, String adminId) {
        this.id = id;
        this.name = name;
        this.adminId = adminId;
    }

    public void update(Long id, ProductCategoryUpdateRequest productCategoryUpdateRequest) {
        this.id = id;
        this.name = productCategoryUpdateRequest.getCategoryName();
        this.adminId = productCategoryUpdateRequest.getAdminId();
    }

}
