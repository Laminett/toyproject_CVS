package com.alliex.cvs.service;

import com.alliex.cvs.domain.type.PointHistoryStatus;
import com.alliex.cvs.entity.PointHistory;
import com.alliex.cvs.repository.PointHistoryRepository;
import com.alliex.cvs.repository.PointHistoryRepositorySupport;
import com.alliex.cvs.exception.PointHistoryProgressAlreadyExistsException;
import com.alliex.cvs.exception.PointHistoryNotFoundException;
import com.alliex.cvs.exception.PointLimitExcessException;
import com.alliex.cvs.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PointHistoryService {

    private final PointHistoryRepository pointHistoryRepository;

    private final PointHistoryRepositorySupport pointHistoryRepositorySupport;

    private final PointService pointService;

    private final Long CHARGE_LIMIT_POINT = 100000L;

    @Transactional(readOnly = true)
    public Page<PointHistoryResponse> getPointHistories(Pageable pageable, PointHistoryRequest pointHistoryRequest) {
        return pointHistoryRepositorySupport.getPointHistories(pageable, pointHistoryRequest).map(PointHistoryResponse::new);
    }

    @Transactional
    public Long update(Long id, PointHistoryUpdateRequest request) {
        PointHistory pointHistory = pointHistoryRepository.findById(id)
                .orElseThrow(() -> new PointHistoryNotFoundException(id));

        pointHistory.update(id, request.getStatus(), request.getAdminId());

        if ("Y".equals(pointHistory.getStatus())) {
            pointService.updatePointPlus(pointHistory.getUser().getId(), pointHistory.getPoint());
        }

        return pointHistory.getId();
    }

    @Transactional
    public PointHistoryProgressResponse progress(Long userId) {
        PointHistory pointHistory = pointHistoryRepository.findByUserIdAndStatus(userId, null)
                .orElseGet(PointHistory::new);

        return new PointHistoryProgressResponse(userId, pointHistory.getPoint());
    }

    @Transactional
    public PointHistorySaveResponse save(PointHistorySaveRequest pointHistorySaveRequest) {
        if (pointHistorySaveRequest.getPoint() > CHARGE_LIMIT_POINT) {
            throw new PointLimitExcessException(pointHistorySaveRequest.getPoint());
        }

        List<PointHistory> pointHistory = pointHistoryRepository.findByUserIdAndStatus(pointHistorySaveRequest.getUserId(), PointHistoryStatus.PROCESSING).stream().collect(Collectors.toList());
        if (pointHistory.size() > 0) {
            throw new PointHistoryProgressAlreadyExistsException();
        }

        pointHistoryRepository.save(pointHistorySaveRequest.toEntity());

        return new PointHistorySaveResponse(pointHistorySaveRequest.getUserId(), pointHistorySaveRequest.getPoint());
    }

}