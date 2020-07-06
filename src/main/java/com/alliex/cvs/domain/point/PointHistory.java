package com.alliex.cvs.domain.point;

import com.alliex.cvs.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Table;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class PointHistory extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Integer point;

    @Column(nullable = false)
    private String registrant;

    @Builder
    public PointHistory(Long userId, Integer point, String registrant) {
        this.userId = userId;
        this.point = point;
        this.registrant = registrant;
    }
}
