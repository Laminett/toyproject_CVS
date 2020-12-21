package com.alliex.cvs.domain.settle;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SettleRepository extends JpaRepository<Settle, Long>, JpaSpecificationExecutor<Settle> {

    Optional<Settle> findById(Long id);

    List<Settle> findByUserIdAndAggregatedAt(Long userId, LocalDate aggregatedAt);

}