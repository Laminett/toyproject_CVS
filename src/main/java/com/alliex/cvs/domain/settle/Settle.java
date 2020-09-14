package com.alliex.cvs.domain.settle;

import com.alliex.cvs.domain.BaseTimeEntity;
import com.alliex.cvs.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class Settle extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(nullable = false)
    private String date;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer approvalCount;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Double approvalAmount;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer cancelCount;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Double cancelAmount;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer totalCount;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Double totalAmount;

    @Column
    private String status;

    @Column
    private String adminId;

    @Builder
    public Settle(Long id, User user, String date, Integer approvalCount, Double approvalAmount, Integer cancelCount, Double cancelAmount, Integer totalCount, Double totalAmount, String status, String adminId) {
        this.id = id;
        this.user = user;
        this.date = date;
        this.approvalCount = approvalCount;
        this.approvalAmount = approvalAmount;
        this.cancelCount = cancelCount;
        this.cancelAmount = cancelAmount;
        this.totalCount = totalCount;
        this.totalAmount = totalAmount;
        this.status = status;
        this.adminId = adminId;
    }

    public void update(Long id, String status, String adminId) {
        this.id = id;
        this.status = status;
        this.adminId = adminId;
    }

}