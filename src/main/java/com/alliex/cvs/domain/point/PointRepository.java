package com.alliex.cvs.domain.point;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PointRepository extends JpaRepository<Point, Long> {

    @Query("select p from Point p order by p.id desc")
    List<Point> findAllDesc();
}
