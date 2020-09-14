package com.alliex.cvs.domain.settle;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettleRepository extends JpaRepository<Settle, Long> {

    Page<Settle> findByDate(Pageable pageable, String date);

}