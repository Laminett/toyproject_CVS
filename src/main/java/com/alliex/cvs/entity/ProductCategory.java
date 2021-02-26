package com.alliex.cvs.entity;

import com.alliex.cvs.domain.BaseTimeEntity;
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

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @Column(nullable = false)
    private Boolean isEnabled;

    private String adminId;

    @OneToMany(mappedBy = "productCategory", cascade = CascadeType.ALL)
    @JsonIgnore
    private Collection<Product> productId;

    @OneToMany(mappedBy = "productCategory", cascade = CascadeType.ALL)
    @JsonIgnore
    private Collection<ProductPurchase> productPurchaseId;

    @Builder
    public ProductCategory(Long id, String name, Boolean isEnabled, String adminId) {
        this.id = id;
        this.name = name;
        this.isEnabled = isEnabled;
        this.adminId = adminId;
    }

    public void update(ProductCategoryUpdateRequest productCategoryUpdateRequest) {
        this.name = productCategoryUpdateRequest.getCategoryName();
        this.isEnabled = productCategoryUpdateRequest.getIsEnabled();
        this.adminId = productCategoryUpdateRequest.getAdminId();
    }

    public Boolean getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Boolean isEnabled) {
        isEnabled = isEnabled;
    }
}
