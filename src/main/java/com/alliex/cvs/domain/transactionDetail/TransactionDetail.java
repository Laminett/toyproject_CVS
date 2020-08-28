package com.alliex.cvs.domain.transactionDetail;

import com.alliex.cvs.domain.BaseTimeEntity;
import com.alliex.cvs.domain.product.Product;
import com.alliex.cvs.domain.transaction.TransState;
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
    private Long transId;

    @Column(nullable = false)
    private Integer productAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransState transState;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @Builder
    public TransactionDetail(Long id, Long transId, Integer productAmount, TransState transState, Product product) {
        this.id = id;
        this.transId = transId;
        this.productAmount = productAmount;
        this.transState = transState;
        this.product = product;
    }

    public void update(TransState transState) {
        this.transState = transState;
    }

}
