package com.alliex.cvs.domain.point;

import com.alliex.cvs.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Point extends BaseTimeEntity {

    @Id
    private Long id;

    @Column(nullable = false)
    private Double point;

    @Builder
    public Point(Long id, Double point) {
        this.id = id;
        this.point = point;
    }

    public void update(Double point) {
        this.point = point;
    }
}
