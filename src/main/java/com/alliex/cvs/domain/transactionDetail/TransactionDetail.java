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
    private Long transactionId;

    @Column(nullable = false)
    private Integer productQuantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionState transactionState;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @Builder
    public TransactionDetail(Long id, Long transactionId, Integer productQuantity, TransactionState transactionState, Product product) {
        this.id = id;
        this.transactionId = transactionId;
        this.productQuantity = productQuantity;
        this.transactionState = transactionState;
        this.product = product;
    }

    public void update(TransactionState transactionState) {
        this.transactionState = transactionState;
    }

}
