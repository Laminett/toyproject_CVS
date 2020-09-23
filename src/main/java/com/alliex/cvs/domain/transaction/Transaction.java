package com.alliex.cvs.domain.transaction;

import com.alliex.cvs.domain.BaseTimeEntity;
import com.alliex.cvs.domain.type.TransactionPaymentType;
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

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(nullable = false)
    private Long merchantId;

    private Long originId;

    @Column(nullable = false)
    private Integer point;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    private String requestId;

    private TransactionPaymentType paymentType;

    @Builder
    public Transaction(Long id, TransactionState state, User user, Long merchantId, Long originId, Integer point, TransactionType type, String requestId, TransactionPaymentType paymentType) {
        this.id = id;
        this.state = state;
        this.user = user;
        this.merchantId = merchantId;
        this.point = point;
        this.type = type;
        this.originId = originId;
        this.requestId = requestId;
        this.paymentType = paymentType;
    }

    public void update(TransactionState state, TransactionPaymentType paymentType) {
        this.state = state;
        this.paymentType = paymentType;
    }

}
