package com.alliex.cvs.domain.product;

import com.alliex.cvs.domain.BaseTimeEntity;
import com.alliex.cvs.domain.product.category.ProductCategory;
import com.alliex.cvs.domain.transactionDetail.TransactionDetail;
import com.alliex.cvs.web.dto.ProductUpdateRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@Entity
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

    private String createdId;

    private String modifiedId;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @JsonIgnore
    private Collection<TransactionDetail> transactionDetailId;

    @Builder
    public Product(Long id, ProductCategory productCategory, String barcode, String name, Long point, Integer quantity, Boolean isEnabled, String createdId, String modifiedId) {
        this.id = id;
        this.productCategory = productCategory;
        this.barcode = barcode;
        this.name = name;
        this.point = point;
        this.quantity = quantity;
        this.isEnabled = isEnabled;
        this.createdId = createdId;
        this.modifiedId = modifiedId;
    }

    public void update(ProductUpdateRequest entity) {
        this.id = entity.getId();
        this.productCategory = entity.toEntity().productCategory;
        this.name = entity.getName();
        this.point = entity.getPoint();
        this.quantity = entity.getQuantity();
        this.isEnabled = entity.getIsEnabled();
        this.modifiedId = entity.getModifiedId();
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
