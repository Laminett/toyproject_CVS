package com.alliex.cvs.repository;

import com.alliex.cvs.domain.type.PointHistoryStatus;
import com.alliex.cvs.entity.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long>, JpaSpecificationExecutor<PointHistory> {

    Optional<PointHistory> findById(Long id);

    Optional<PointHistory> findByUserIdAndStatus(Long userId, PointHistoryStatus status);

}