package com.alliex.cvs.entity;

import com.alliex.cvs.domain.BaseTimeEntity;
import com.alliex.cvs.entity.User;
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
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(nullable = false)
    private Long point;

    @Column
    private String status;

    @Column
    private String adminId;

    @Builder
    public PointHistory(Long id, User user, Long point, String status, String adminId) {
        this.id = id;
        this.user = user;
        this.point = point;
        this.status = status;
        this.adminId = adminId;
    }

    public void update(Long id, String status, String adminId) {
        this.id = id;
        this.status = status;
        this.adminId = adminId;
    }

}