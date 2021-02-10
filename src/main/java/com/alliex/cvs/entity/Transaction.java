package com.alliex.cvs.entity;

import com.alliex.cvs.domain.BaseTimeEntity;
import com.alliex.cvs.domain.type.PaymentType;
import com.alliex.cvs.domain.type.TransactionState;
import com.alliex.cvs.domain.type.TransactionType;
import com.alliex.cvs.entity.User;
import lombok.*;

import javax.persistence.*;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(indexes = {
        @Index(name = "uix_request_id", columnList = "requestId", unique = true)
})
public class Transaction extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionState state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_transaction_user_id"))
    private User user;

    private String originRequestId;

    @Column(nullable = false)
    private Long point;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @Column(nullable = false)
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
