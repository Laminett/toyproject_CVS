package com.alliex.cvs.domain.transaction;

import com.alliex.cvs.domain.BaseTimeEntity;
import com.alliex.cvs.domain.user.User;
import lombok.*;

import javax.persistence.*;


@Getter
@Setter
@NoArgsConstructor
@Entity
public class Transaction extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransState transState;

    @ManyToOne
    @JoinColumn(name = "buyer_id", referencedColumnName = "id")
    private User user;

    @Column(nullable = false)
    private Long merchantId;

    private Long originId;

    @Column(nullable = false)
    private Integer transPoint;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransType transType;

    private String transNumber;

    private String paymentType;

    @Builder
    public Transaction(Long id, TransState transState, User user, Long merchantId, Long originId, Integer transPoint, TransType transType, String transNumber, String paymentType) {
        this.id = id;
        this.transState = transState;
        this.user = user;
        this.merchantId = merchantId;
        this.transPoint = transPoint;
        this.transType = transType;
        this.originId = originId;
        this.transNumber = transNumber;
        this.paymentType = paymentType;
    }

    public void update(TransState transState) {
        this.transState = transState;
    }

}
