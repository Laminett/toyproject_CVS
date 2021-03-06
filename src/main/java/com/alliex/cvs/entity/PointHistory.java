package com.alliex.cvs.entity;

import com.alliex.cvs.domain.BaseTimeEntity;
import com.alliex.cvs.domain.type.PointHistoryStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class PointHistory extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_point_history_user_id"))
    private User user;

    @Column(nullable = false)
    private Long point;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PointHistoryStatus status;

    @Column
    private String adminId;

    @Builder
    public PointHistory(Long id, User user, Long point, PointHistoryStatus status, String adminId) {
        this.id = id;
        this.user = user;
        this.point = point;
        this.status = status;
        this.adminId = adminId;
    }

    public void update(Long id, PointHistoryStatus status, String adminId) {
        this.id = id;
        this.status = status;
        this.adminId = adminId;
    }

}