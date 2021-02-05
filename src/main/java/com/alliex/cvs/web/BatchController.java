package com.alliex.cvs.web;

import com.alliex.cvs.service.SettleService;
import com.alliex.cvs.util.DateTimeUtils;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

@EnableScheduling
@RestController
public class BatchController {

    private static final Logger logger = LoggerFactory.getLogger(BatchController.class);

    @Autowired
    private SettleService settleService;

    // 매달 1일 00시 10분
    @Scheduled(cron = "0 10 0 1 * *")
    public void settleBatch() {
        int batchSize = settleBatchManual(LocalDate.now().minusMonths(1L));
        logger.info("Settle batch insert success. total: " + batchSize);
    }

    @ApiOperation(value = "Manual Settle Batch", notes = "수동 정산 배치")
    @PostMapping("/web-api/v1/system/batch/manual/settle/{aggregatedAt}")
    public Integer settleBatchManual(@PathVariable @DateTimeFormat(pattern = "ddMMyyyy") LocalDate aggregatedAt) {
        int batchSize = settleService.transactionMonthlySum(DateTimeUtils.getFirstDayOfMonth(aggregatedAt.atStartOfDay()), aggregatedAt.atStartOfDay().with(TemporalAdjusters.firstDayOfNextMonth()));

        return batchSize;
    }

}