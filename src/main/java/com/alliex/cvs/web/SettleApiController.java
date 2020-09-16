package com.alliex.cvs.web;

import com.alliex.cvs.service.SettleService;
import com.alliex.cvs.web.dto.SettleResponse;
import com.alliex.cvs.web.dto.SettleSaveRequest;
import com.alliex.cvs.web.dto.SettleUpdateRequest;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SettleApiController {

    @Autowired
    private SettleService settleService;

    @ApiOperation(value = "Get Settle List", notes = "정산 목록")
    @GetMapping("/web-api/v1/settle")
    public List<SettleResponse> getSettleList(@RequestParam(value = "page") int page, @RequestParam(value = "searchDate") String date, @RequestParam(value = "searchUsername") String username) {
        return settleService.getSettleList(PageRequest.of(page - 1, 20, Sort.Direction.DESC, "id"), date, username);
    }

    @PutMapping("/web-api/v1/settle/{id}")
    public Long SettleUpdate(@PathVariable Long id, @RequestBody SettleUpdateRequest request) {
        return settleService.update(id, request);
    }

}