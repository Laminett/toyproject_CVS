package com.alliex.cvs.entity;

import com.alliex.cvs.domain.BaseTimeEntity;
import com.alliex.cvs.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Entity
@NoArgsConstructor
public class Settle extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(nullable = false)
    private LocalDate aggregatedAt;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer approvalCount;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Long approvalAmount;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer cancelCount;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Long cancelAmount;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer totalCount;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Long totalAmount;

    @Column
    private String status;

    @Column
    private String adminId;

    @Builder
    public Settle(Long id, User user, LocalDate aggregatedAt, Integer approvalCount, Long approvalAmount, Integer cancelCount, Long cancelAmount, Integer totalCount, Long totalAmount, String status, String adminId) {
        this.id = id;
        this.user = user;
        this.aggregatedAt = aggregatedAt;
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