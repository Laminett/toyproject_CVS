package com.alliex.cvs.service;

import com.alliex.cvs.domain.point.PointHistoryRepository;
import com.alliex.cvs.web.dto.PointHistoryRequest;
import com.alliex.cvs.web.dto.PointHistoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PointHistoryService {

    private final PointHistoryRepository pointHistoryRepository;

    @Transactional
    public Long save(PointHistoryRequest pointHistoryRequest) {
        return pointHistoryRepository.save(pointHistoryRequest.toEntity()).getUserId();
    }

    @Transactional
    public List<PointHistoryResponse> findByUserId(Long userId){
        return pointHistoryRepository.findByUserIdOrderByIdDesc(userId).stream()
                .map(PointHistoryResponse::new)
                .collect(Collectors.toList());
    }

}