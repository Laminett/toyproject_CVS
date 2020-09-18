package com.alliex.cvs.domain.transaction;

import com.alliex.cvs.domain.BaseTimeEntity;
import com.alliex.cvs.domain.type.TransState;
import com.alliex.cvs.domain.type.TransType;
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
    private TransState state;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(nullable = false)
    private Long merchantId;

    private Long originid;

    @Column(nullable = false)
    private Integer point;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransType type;

    private String requestid;

    private String paymentType;

    @Builder
    public Transaction(Long id, TransState state, User user, Long merchantId, Long originid, Integer point, TransType type, String requestid, String paymentType) {
        this.id = id;
        this.state = state;
        this.user = user;
        this.merchantId = merchantId;
        this.point = point;
        this.type = type;
        this.originid = originid;
        this.requestid = requestid;
        this.paymentType = paymentType;
    }

    public void update(TransState state) {
        this.state = state;
    }

}
