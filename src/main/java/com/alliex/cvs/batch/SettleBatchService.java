package com.alliex.cvs.batch;

import com.alliex.cvs.domain.settle.SettleRepository;
import com.alliex.cvs.domain.transaction.TransactionRepository;
import com.alliex.cvs.util.DateTimeUtils;
import com.alliex.cvs.web.dto.SettleBatchRequest;
import com.alliex.cvs.web.dto.SettleTransMonthlySumRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SettleBatchService {

    private static final Logger logger = LoggerFactory.getLogger(SettleBatchService.class);

    private final TransactionRepository transactionRepository;

    private final SettleRepository settleRepository;

    @Scheduled(cron = "0 0 7 1 *")
    public void settleBatch() {

        LocalDateTime today = LocalDateTime.now();
        int batchSize = this.transactionMonthlySum(DateTimeUtils.getFirstDayOfPrevMonth(today), DateTimeUtils.getFirstDayOfMonth(today));
        logger.info("Insert success. total: " + batchSize);
    }

    @Transactional
    public Integer transactionMonthlySum(LocalDateTime fromDate, LocalDateTime toDate) {

        String aggregatedAt = fromDate.format(DateTimeFormatter.ofPattern("yyyyMM"));

        List<SettleTransMonthlySumRequest> transMonthlySumList = transactionRepository.findByCreatedDate(fromDate, toDate);
        int listSize = transMonthlySumList.size();
        if (listSize == 0) {
            return listSize;
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

            try {
                settleRepository.save(settleBatchRequest.toEntity());
            } catch (Exception e) {
                logger.error("User Id: " + obj.getUserId() + " is not inserted in settle table.");
            }
        }

        return listSize;
    }
}