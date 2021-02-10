package com.alliex.cvs.entity;

import com.alliex.cvs.domain.BaseTimeEntity;
import com.alliex.cvs.web.dto.ProductUpdateRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(indexes = {
        @Index(name = "uix_barcode", columnList = "barcode", unique = true),
        @Index(name = "ix_name", columnList = "name")
})
public class Product extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private ProductCategory productCategory;

    @Column(nullable = false)
    private String barcode;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long point;

    @Column(nullable = false)
    private Integer quantity;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @Column(nullable = false)
    private Boolean isEnabled;

    private String adminId;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @JsonIgnore
    private Collection<TransactionDetail> transactionDetailId;

    @OneToMany(mappedBy = "product")
    @JsonIgnore
    private Collection<ProductPurchase> productPurchaseId;

    @Builder
    public Product(Long id, ProductCategory productCategory, String barcode, String name, Long point, Integer quantity, Boolean isEnabled, String adminId) {
        this.id = id;
        this.productCategory = productCategory;
        this.barcode = barcode;
        this.name = name;
        this.point = point;
        this.quantity = quantity;
        this.isEnabled = isEnabled;
        this.adminId = adminId;
    }

    public void update(ProductUpdateRequest entity) {
        this.productCategory = entity.toEntity().productCategory;
        this.name = entity.getName();
        this.point = entity.getPoint();
        this.quantity = entity.getQuantity();
        this.isEnabled = entity.getIsEnabled();
        this.adminId = entity.getAdminId();
    }

    public void updateQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Boolean getEnabled() {
        return isEnabled;
    }

    public void setEnabled(Boolean enabled) {
        isEnabled = enabled;
    }

}
