package com.alliex.cvs.domain.product;

import com.alliex.cvs.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Product extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(nullable = false)
    private String categoryid;

    @Column(nullable = false)
    private String barcode;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String price;

    @Column(nullable = false)
    private String useyn;

    private String created_id;

    private String modified_id;


    @Builder
    public Product(String categoryid, String barcode, String name, String price, String useyn, String created_id, String modified_id){
        this.categoryid=categoryid;
        this.barcode=barcode;
        this.name=name;
        this.price=price;
        this.useyn=useyn;
        this.created_id=created_id;
        this.modified_id=modified_id;
    }
}
