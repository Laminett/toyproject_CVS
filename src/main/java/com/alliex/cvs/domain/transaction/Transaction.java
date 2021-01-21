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

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    private String originRequestId;

    @Column(nullable = false)
    private Long point;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    private String requestId;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @Builder
    public Transaction(Long id, TransactionState transactionState, User user, String originRequestId, Long point, TransactionType transactionType, String requestId, PaymentType paymentType) {
        this.id = id;
        this.state = transactionState;
        this.user = user;
        this.point = point;
        this.type = transactionType;
        this.originRequestId = originRequestId;
        this.requestId = requestId;
        this.paymentType = paymentType;
    }

}
