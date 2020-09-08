package com.alliex.cvs.domain.point;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {

    Page<PointHistory> findAll(Pageable pageable);

    Page<PointHistory> findByUserId(Pageable pageable, Long userId);

    Page<PointHistory> findByStatus(Pageable pageable, String status);

    Page<PointHistory> findByUserIdAndStatus(Pageable pageable, Long userId, String status);

    Optional<PointHistory> findById(Long id);

}