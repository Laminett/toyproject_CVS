package com.alliex.cvs.service;

import com.alliex.cvs.domain.settle.Settle;
import com.alliex.cvs.domain.settle.SettleRepository;
import com.alliex.cvs.domain.settle.SettleSpecification;
import com.alliex.cvs.exception.SettleNotFoundException;
import com.alliex.cvs.web.dto.SettleRequest;
import com.alliex.cvs.web.dto.SettleResponse;
import com.alliex.cvs.web.dto.SettleUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class SettleService {

    private final SettleRepository settleRepository;

    @Transactional(readOnly = true)
    public Page<SettleResponse> getSettleList(Pageable pageable, SettleRequest settleRequest) {
        return settleRepository.findAll(Specification
                .where(StringUtils.isBlank(settleRequest.getAggregatedAt()) ? null : SettleSpecification.withSearchData("aggregatedAt", settleRequest.getAggregatedAt()))
                .and(StringUtils.isBlank(settleRequest.getUsername()) ? null : SettleSpecification.withSearchData("username", settleRequest.getUsername())), pageable)
                .map(SettleResponse::new);
    }

    @Transactional
    public Long update(Long id, SettleUpdateRequest request) {
        Settle settle = settleRepository.findById(id)
                .orElseThrow(() -> new SettleNotFoundException("not found settle. id: " + id));

        settle.update(id, request.getStatus(), request.getAdminId());

        return settle.getId();
    }

}