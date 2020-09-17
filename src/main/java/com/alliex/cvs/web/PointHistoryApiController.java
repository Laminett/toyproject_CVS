package com.alliex.cvs.web;

import com.alliex.cvs.service.PointHistoryService;
import com.alliex.cvs.web.dto.PointHisotryUpdateRequest;
import com.alliex.cvs.web.dto.PointHistorySaveRequest;
import com.alliex.cvs.web.dto.PointHistoryResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PointHistoryApiController {

    @Autowired
    private PointHistoryService pointHistoryService;

    @ApiOperation(value = "Get Point History", notes = "충전 요청 목록")
    @GetMapping("/web-api/v1/point/history")
    public List<PointHistoryResponse> getPointHistory(@RequestParam(value = "page") int page, @RequestParam(value = "searchStatus") String status, @RequestParam(value = "searchUsername") String username) {
        return pointHistoryService.getPointHistory(PageRequest.of(page - 1, 20, Sort.Direction.DESC, "id"), status, username);
    }

    @ApiOperation(value = "Update Point History", notes = "충전 요청 업데이트")
    @PutMapping("/web-api/v1/point/history/{id}")
    public Long PointHistoryUpdate(@PathVariable Long id, @RequestBody PointHisotryUpdateRequest pointHisotryUpdateRequest) {
        return pointHistoryService.update(id, pointHisotryUpdateRequest);
    }

    @PostMapping("/api/v1/point/history/save")
    public Long PointHistorySave(@RequestBody PointHistorySaveRequest pointHistorySaveRequest) {
        return pointHistoryService.save(pointHistorySaveRequest);
    }

}