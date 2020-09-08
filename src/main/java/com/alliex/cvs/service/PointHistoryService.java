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

        long userId = 2;

        Page<PointHistory> getPage = null;
        if(StringUtils.isBlank(status)) {
            if(StringUtils.isBlank(userName)){
                getPage = pointHistoryRepository.findAll(pageable);
            }else{
                getPage = pointHistoryRepository.findByUserId(pageable, userId);
            }
        }else{
            if(StringUtils.isBlank(userName)){
                getPage = pointHistoryRepository.findByStatus(pageable, status);
            }else{
                getPage = pointHistoryRepository.findByUserIdAndStatus(pageable, userId, status);
            }
        }

        List<Integer> pages = new ArrayList<>();
        for(int i = 1; i<= getPage.getTotalPages(); i++) {
            pages.add(i);
        }

        return pages;
    }

    @Transactional(readOnly = true)
    public List<PointHistoryResponse> getPointHistory(Pageable pageable, String status, String userName) {
        long userId = 2;

        if("undefined".equals(userName)){
            userName = "";
        }

        List<PointHistoryResponse> pointHistoryList = null;
        if(StringUtils.isBlank(status)) {
            if(StringUtils.isBlank(userName)){
                pointHistoryList = pointHistoryRepository.findAll(pageable).stream().map(PointHistoryResponse::new).collect(Collectors.toList());
            }else{
                pointHistoryList = pointHistoryRepository.findByUserId(pageable, userId).stream().map(PointHistoryResponse::new).collect(Collectors.toList());
            }
        }else {
            if(StringUtils.isBlank(userName)){
                pointHistoryList = pointHistoryRepository.findByStatus(pageable, status).stream().map(PointHistoryResponse::new).collect(Collectors.toList());
            }else{
                pointHistoryList = pointHistoryRepository.findByUserIdAndStatus(pageable, userId, status).stream().map(PointHistoryResponse::new).collect(Collectors.toList());
            }
        }

        for (PointHistoryResponse pointHistoryResponse : pointHistoryList){

            System.out.println(pointHistoryResponse.toString());

            if("Y".equals(pointHistoryResponse.getStatus())) {
                pointHistoryResponse.setIsApproved("Y");
            }

            pointHistoryResponse.setRequestDate(pointHistoryResponse.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:dd")));
            if(pointHistoryResponse.getModifiedDate() != null) {
                pointHistoryResponse.setCheckDate(pointHistoryResponse.getModifiedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:dd")));
            }
        }
        return pointHistoryList;
    }

    @Transactional
    public boolean update(Long id, PointHisotryUpdateRequest request){
        PointHistory pointHistory = pointHistoryRepository.findById(id)
                .orElseThrow(() -> new PointHistoryNotFoundException("not found pointHistory. id: " + id));

        System.out.println();

        //pointHistory.update(id, request.getStatus(), request.getAdminId());


        if("Y".equals(pointHistory.getStatus())){
            //pointService.updatePointPlus(pointHistory.getUser().getId(), pointHistory.getPoint());
        }

        return  true;
    }

    @Transactional
    public Long save(PointHistorySaveRequest pointHistorySaveRequest) {
        return pointHistoryRepository.save(pointHistorySaveRequest.toEntity()).getId();
    }

}