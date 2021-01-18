package com.alliex.cvs.service;

import com.alliex.cvs.domain.point.PointHistory;
import com.alliex.cvs.domain.point.PointHistoryRepository;
import com.alliex.cvs.domain.point.PointHistorySpecification;
import com.alliex.cvs.exception.PointHistoryNotFoundException;
import com.alliex.cvs.exception.PointLimitExcessException;
import com.alliex.cvs.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PointHistoryService {

    private final PointHistoryRepository pointHistoryRepository;

    private final PointService pointService;

    private final Long CHARGE_LIMIT_POINT = 100000L;

    @Transactional(readOnly = true)
    public Page<PointHistoryResponse> getPointHistories(Pageable pageable, PointHistoryRequest pointHistoryRequest) {
        return pointHistoryRepository.findAll(Specification
                .where(PointHistorySpecification.withSearchPeriod("createdDate", pointHistoryRequest))
                .and(StringUtils.isBlank(pointHistoryRequest.getStatus()) ? null : PointHistorySpecification.withSearchData("status", pointHistoryRequest.getStatus()))
                .and(StringUtils.isBlank(pointHistoryRequest.getFullName()) ? null : PointHistorySpecification.withSearchData("fullName", pointHistoryRequest.getFullName())), pageable)
                .map(PointHistoryResponse::new);
    }

    @Transactional
    public Long update(Long id, PointHisotryUpdateRequest request) {
        PointHistory pointHistory = pointHistoryRepository.findById(id)
                .orElseThrow(() -> new PointHistoryNotFoundException("not found pointHistory. id: " + id));

        pointHistory.update(id, request.getStatus(), request.getAdminId());

        if ("Y".equals(pointHistory.getStatus())) {
            pointService.updatePointPlus(pointHistory.getUser().getId(), pointHistory.getPoint());
        }

        return pointHistory.getId();
    }

    @Transactional
    public PointHistorySaveResponse save(PointHistorySaveRequest pointHistorySaveRequest) {
        if (pointHistorySaveRequest.getPoint() > CHARGE_LIMIT_POINT) {
            throw new PointLimitExcessException("The point is too much to charge. point: " + pointHistorySaveRequest.getPoint());
        }
        pointHistoryRepository.save(pointHistorySaveRequest.toEntity());

        return new PointHistorySaveResponse(pointHistorySaveRequest.getUserId(), pointHistorySaveRequest.getPoint());
    }

}