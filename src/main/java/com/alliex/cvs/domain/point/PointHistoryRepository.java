package com.alliex.cvs.domain.point;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long>, JpaSpecificationExecutor<PointHistory> {

    Optional<PointHistory> findById(Long id);

    Optional<PointHistory> findByUserIdAndStatus(Long userId, String status);

}