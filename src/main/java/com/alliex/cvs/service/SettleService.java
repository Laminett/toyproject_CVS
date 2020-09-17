package com.alliex.cvs.service;

import com.alliex.cvs.domain.settle.Settle;
import com.alliex.cvs.domain.settle.SettleRepository;
import com.alliex.cvs.domain.settle.SettleSpecification;
import com.alliex.cvs.exception.SettleNotFoundException;
import com.alliex.cvs.web.dto.SettleResponse;
import com.alliex.cvs.web.dto.SettleUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SettleService {

    private final SettleRepository settleRepository;

    @Transactional(readOnly = true)
    public Page<SettleResponse> getSettleList(Pageable pageable, String aggregatedAt, String username) {
        return settleRepository.findAll(Specification
                .where(StringUtils.isBlank(aggregatedAt) ? null : SettleSpecification.withSearchData("aggregatedAt", aggregatedAt))
                .and(StringUtils.isBlank(username) ? null : SettleSpecification.withSearchData("username", username)), pageable)
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