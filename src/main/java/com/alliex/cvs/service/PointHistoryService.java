package com.alliex.cvs.service;

import com.alliex.cvs.domain.point.PointHistory;
import com.alliex.cvs.domain.point.PointHistoryRepository;
import com.alliex.cvs.exception.PointHistoryNotFoundException;
import com.alliex.cvs.web.dto.PointHisotryUpdateRequest;
import com.alliex.cvs.web.dto.PointHistorySaveRequest;
import com.alliex.cvs.web.dto.PointHistoryResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PointHistoryService {

    private final PointHistoryRepository pointHistoryRepository;

    private final PointService pointService;

    @Transactional(readOnly = true)
    public List<Integer> getPage(Pageable pageable, String status, String userName) {
        Page<PointHistory> getPage = null;
        if (StringUtils.isNotBlank(userName)) {
            if (StringUtils.isNotBlank(status)) {
                getPage = pointHistoryRepository.findByStatusAndUserUsername(pageable, status, userName);
            } else {
                getPage = pointHistoryRepository.findByUserUsername(pageable, userName);
            }
        } else {
            if (StringUtils.isNotBlank(status)) {
                getPage = pointHistoryRepository.findByStatus(pageable, status);
            } else {
                getPage = pointHistoryRepository.findAll(pageable);
            }
        }

        List<Integer> pages = new ArrayList<>();
        for (int i = 1; i <= getPage.getTotalPages(); i++) {
            pages.add(i);
        }

        return pages;
    }

    @Transactional(readOnly = true)
    public List<PointHistoryResponse> getPointHistory(Pageable pageable, String status, String userName) {
        List<PointHistoryResponse> pointHistoryList = null;
        if (StringUtils.isNotBlank(userName)) {
            if (StringUtils.isNotBlank(status)) {
                pointHistoryList = pointHistoryRepository.findByStatusAndUserUsername(pageable, status, userName).stream().map(PointHistoryResponse::new).collect(Collectors.toList());
            } else {
                pointHistoryList = pointHistoryRepository.findByUserUsername(pageable, userName).stream().map(PointHistoryResponse::new).collect(Collectors.toList());
            }
        } else {
            if (StringUtils.isNotBlank(status)) {
                pointHistoryList = pointHistoryRepository.findByStatus(pageable, status).stream().map(PointHistoryResponse::new).collect(Collectors.toList());
            } else {
                pointHistoryList = pointHistoryRepository.findAll(pageable).stream().map(PointHistoryResponse::new).collect(Collectors.toList());
            }

        }

        for (PointHistoryResponse pointHistoryResponse : pointHistoryList) {
            if ("Y".equals(pointHistoryResponse.getStatus())) {
                pointHistoryResponse.setIsApproved("Y");
            }

            pointHistoryResponse.setRequestDate(pointHistoryResponse.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:dd")));
            if (pointHistoryResponse.getModifiedDate() != null) {
                pointHistoryResponse.setUpdateDate(pointHistoryResponse.getModifiedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:dd")));
            }
        }

        return pointHistoryList;
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
    public Long save(PointHistorySaveRequest pointHistorySaveRequest) {
        return pointHistoryRepository.save(pointHistorySaveRequest.toEntity()).getId();
    }

}