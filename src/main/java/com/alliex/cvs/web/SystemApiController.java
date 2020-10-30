package com.alliex.cvs.web;

import com.alliex.cvs.batch.SettleBatchService;
import com.alliex.cvs.util.DateTimeUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

@RestController
public class SystemApiController {

    @Autowired
    private SettleBatchService settleBatchService;

    @ApiOperation(value = "Manual Settle Batch", notes = "수동 정산 배치")
    @PostMapping("/web-api/v1/system/batch/manual/settle")
    public Integer settleBatchManual(@RequestBody String aggregatedAt) {
        LocalDateTime batchDate = DateTimeUtils.stringToLocalDateTime(aggregatedAt);
        int batchSize = settleBatchService.transactionMonthlySum(DateTimeUtils.getFirstDayOfMonth(batchDate), batchDate.with(TemporalAdjusters.firstDayOfNextMonth()));

        return batchSize;
    }

}