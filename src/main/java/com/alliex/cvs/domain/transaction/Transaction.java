package com.alliex.cvs.domain.transaction;

import com.alliex.cvs.domain.BaseTimeEntity;
import com.alliex.cvs.domain.type.PaymentType;
import com.alliex.cvs.domain.type.TransactionState;
import com.alliex.cvs.domain.type.TransactionType;
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
    private TransactionState state;

    // User -> Transaction 관계 임시 해제를 위한 주석
//    @ManyToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Long userId;

    @Column(nullable = false)
    private Long merchantId;

    private Long originId;

    @Column(nullable = false)
    private Long point;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    private String requestId;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @Builder
    public Transaction(Long id, TransactionState transactionState, Long userId, Long merchantId, Long originId, Long point, TransactionType transactionType, String requestId, PaymentType paymentType) {
        this.id = id;
        this.state = transactionState;
        this.userId = userId;
        this.merchantId = merchantId;
        this.point = point;
        this.type = transactionType;
        this.originId = originId;
        this.requestId = requestId;
        this.paymentType = paymentType;
    }

    public void update(Long userId, TransactionState transactionState) {
        this.userId = userId;
        this.state = transactionState;
    }

}
