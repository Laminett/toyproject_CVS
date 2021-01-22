package com.alliex.cvs.domain.transactionDetail;

import com.alliex.cvs.domain.BaseTimeEntity;
import com.alliex.cvs.domain.product.Product;
import com.alliex.cvs.domain.type.TransactionState;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class TransactionDetail extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String requestId;

    @Column(nullable = false)
    private Integer productQuantity;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @Builder
    public TransactionDetail(Long id, String requestId, Integer productQuantity, Product product) {
        this.id = id;
        this.requestId = requestId;
        this.productQuantity = productQuantity;
        this.product = product;
    }

}
