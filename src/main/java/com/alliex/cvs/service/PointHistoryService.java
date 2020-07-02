package com.alliex.cvs.service;

import com.alliex.cvs.domain.point.PointHistory;
import com.alliex.cvs.domain.point.PointHistoryRepository;
import com.alliex.cvs.web.dto.PointHistoryRequest;
import com.alliex.cvs.web.dto.PointHistoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PointHistoryService {

    private final PointHistoryRepository pointHistoryRepository;

    @Transactional
    public Long save(PointHistoryRequest pointHistoryRequest) {
        return pointHistoryRepository.save(pointHistoryRequest.toEntity()).getId();
    }

    @Transactional(readOnly = true)
    public PointHistoryResponse findById(Long id) {
        PointHistory entity = pointHistoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found point histroy. id: " + id));
        return new PointHistoryResponse(entity);
    }

}