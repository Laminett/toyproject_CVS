package com.alliex.cvs.web;

import com.alliex.cvs.service.PointHistoryService;
import com.alliex.cvs.web.dto.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
public class PointHistoryApiController {

    @Autowired
    private PointHistoryService pointHistoryService;

    @ApiOperation(value = "Get Point History", notes = "충전 요청 목록")
    @GetMapping("/web-api/v1/point/history")
    public Page<PointHistoryResponse> getPointHistories(@RequestParam(required = false, defaultValue = "1") int page, PointHistoryRequest pointHistoryRequest) {
        return pointHistoryService.getPointHistories(PageRequest.of(page - 1, 10, Sort.Direction.DESC, "id"), pointHistoryRequest);
    }

    @ApiOperation(value = "Update Point History", notes = "충전 요청 업데이트")
    @PutMapping("/web-api/v1/point/history/{id}")
    public Long PointHistoryUpdate(@PathVariable Long id, @RequestBody PointHistoryUpdateRequest pointHistoryUpdateRequest) {
        return pointHistoryService.update(id, pointHistoryUpdateRequest);
    }

    @GetMapping(value = "/api/v1/point/history/progress/users/{id}")
    public PointHistoryProgressResponse PointHistorySearch(@PathVariable("id") Long userId) {
        return pointHistoryService.progress(userId);
    }

    @PostMapping("/api/v1/point/history")
    public PointHistorySaveResponse PointHistorySave(@RequestBody PointHistorySaveRequest pointHistorySaveRequest) {
        return pointHistoryService.save(pointHistorySaveRequest);
    }

}