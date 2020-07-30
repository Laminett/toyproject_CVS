package com.alliex.cvs.domain.product;

import com.alliex.cvs.domain.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Getter
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
    private Integer point;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @Column(nullable = false)
    private Boolean isEnabled;

    private String createdId;

    private String modifiedId;

    @Builder
    public Product(Long id, String categoryId, String barcode, String name, Integer point, Boolean isEnabled, String createdId, String modifiedId) {
        this.id = id;
        this.categoryId = categoryId;
        this.barcode = barcode;
        this.name = name;
        this.point = point;
        this.isEnabled = isEnabled;
        this.createdId = createdId;
        this.modifiedId = modifiedId;
    }

    public void update(Long id, String categoryId, String name, Integer point, boolean isEnabled, String modifiedId) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
        this.point = point;
        this.isEnabled = isEnabled;
        this.modifiedId = modifiedId;
    }

    public Boolean getEnabled() {
        return isEnabled;
    }

    public void setEnabled(Boolean enabled) {
        isEnabled = enabled;
    }

}
