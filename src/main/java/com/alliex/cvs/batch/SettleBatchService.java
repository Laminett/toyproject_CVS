package com.alliex.cvs.batch;

import com.alliex.cvs.domain.settle.SettleRepository;
import com.alliex.cvs.domain.transaction.TransactionRepository;
import com.alliex.cvs.util.DateTimeUtils;
import com.alliex.cvs.web.dto.SettleBatchRequest;
import com.alliex.cvs.web.dto.SettleTransMonthlySumRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@EnableScheduling
@RequiredArgsConstructor
public class SettleBatchService {

    private static final Logger logger = LoggerFactory.getLogger(SettleBatchService.class);

    private final TransactionRepository transactionRepository;

    private final SettleRepository settleRepository;

    // 매달 1일 00시 10분
    @Scheduled(cron = "0 10 0 1 * *")
    public void settleBatch() {
        LocalDateTime today = LocalDateTime.now();
        int batchSize = this.transactionMonthlySum(DateTimeUtils.getFirstDayOfPrevMonth(today), DateTimeUtils.getFirstDayOfMonth(today));
        logger.info("Settle batch insert success. total: " + batchSize);
    }

    @Transactional
    public Integer transactionMonthlySum(LocalDateTime fromDate, LocalDateTime toDate) {

        String aggregatedAt = fromDate.format(DateTimeFormatter.ofPattern("yyyyMM"));

        List<SettleTransMonthlySumRequest> transMonthlySumList = transactionRepository.findByCreatedDate(fromDate, toDate);
        if (CollectionUtils.isEmpty(transMonthlySumList)) {
            return 0;
        }

        SettleBatchRequest settleBatchRequest = new SettleBatchRequest();
        for (SettleTransMonthlySumRequest obj : transMonthlySumList) {
            settleBatchRequest.setUserId(obj.getUserId());
            settleBatchRequest.setAggregatedAt(aggregatedAt);
            settleBatchRequest.setApprovalCount(obj.getApprovalCount().intValue());
            settleBatchRequest.setApprovalAmount(obj.getApprovalAmount());
            settleBatchRequest.setCancelCount(obj.getCancelCount().intValue());
            settleBatchRequest.setCancelAmount(obj.getCancelAmount());
            settleBatchRequest.setTotalCount(obj.getTotalCount().intValue());
            settleBatchRequest.setTotalAmount(obj.getTotalAmount());

            settleRepository.save(settleBatchRequest.toEntity());
        }

        return transMonthlySumList.size();
    }
}