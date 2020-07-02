package com.alliex.cvs.domain.point;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {

    @Query("select p from PointHistory p order by p.id desc")
    List<PointHistory> findAllDesc();

}
