package com.alliex.cvs.domain.product;

import com.alliex.cvs.domain.BaseTimeEntity;
import com.alliex.cvs.domain.transactionDetail.TransactionDetail;
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

    @Column(nullable = false)
    private String categoryId;

    @Column(nullable = false)
    private String barcode;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal point;

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
    public Product(Long id, String categoryId, String barcode, String name, BigDecimal point, Integer quantity, Boolean isEnabled, String createdId, String modifiedId) {
        this.id = id;
        this.categoryId = categoryId;
        this.barcode = barcode;
        this.name = name;
        this.point = point;
        this.quantity = quantity;
        this.isEnabled = isEnabled;
        this.createdId = createdId;
        this.modifiedId = modifiedId;
    }

    public void update(Long id, String categoryId, String name, BigDecimal point, Integer quantity, boolean isEnabled, String modifiedId) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
        this.point = point;
        this.quantity = quantity;
        this.isEnabled = isEnabled;
        this.modifiedId = modifiedId;
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
