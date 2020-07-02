package com.alliex.cvs.domain.point;

import com.alliex.cvs.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Table;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@NoArgsConstructor
@Entity
public class PointHistory extends BaseTimeEntity {

    @Id
    private Long id;

    @Column(nullable = false)
    private Double point;

    @Column(nullable = false)
    private String registrant;

    @Builder
    public PointHistory(Long id, Double point, String registrant) {
        this.id = id;
        this.point = point;
        this.registrant = registrant;
    }
}
