package com.alliex.cvs.domain.product;

import com.alliex.cvs.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Products extends BaseTimeEntity {

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

    @Column(nullable = false)
    private Boolean isEnabled;

    private String createdId;

    private String modifiedId;

    @Builder
    public Products(Long id, String categoryId, String barcode, String name, Integer point, Boolean isEnabled, String createdId, String modifiedId) {
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
}
