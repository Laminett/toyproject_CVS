package com.alliex.cvs.domain.product.category;

import com.alliex.cvs.domain.BaseTimeEntity;
import com.alliex.cvs.domain.product.Product;
import com.alliex.cvs.domain.transactionDetail.TransactionDetail;
import com.alliex.cvs.web.dto.ProductCategoryUpdateRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@Entity
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

    @Builder
    public ProductCategory(Long id, String name, Boolean isEnabled, String adminId) {
        this.id = id;
        this.name = name;
        this.isEnabled = isEnabled;
        this.adminId = adminId;
    }

    public void update(Long id, ProductCategoryUpdateRequest productCategoryUpdateRequest) {
        this.id = id;
        this.name = productCategoryUpdateRequest.getCategoryName();
        this.isEnabled = productCategoryUpdateRequest.getIsEnabled();
        this.adminId = productCategoryUpdateRequest.getAdminId();
    }

    public Boolean getEnabled() {
        return isEnabled;
    }

    public void setEnabled(Boolean enabled) {
        isEnabled = enabled;
    }

}
