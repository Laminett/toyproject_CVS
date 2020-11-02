package com.alliex.cvs.service;

import com.alliex.cvs.domain.transaction.TransactionRepository;
import com.alliex.cvs.util.DateTimeUtils;
import com.alliex.cvs.web.dto.SettleTransMonthlySumRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional()
@Rollback()
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:test-data.sql"),
})
public class SettleBatchServiceTest {

    @Autowired
    TransactionRepository transactionRepository;

    @Test
    public void queryTest() {
        LocalDateTime fromDate = LocalDateTime.of(LocalDate.parse("20200922", DateTimeFormatter.ofPattern("yyyyMMdd")), LocalTime.of(0, 0, 0));
        LocalDateTime toDate = LocalDateTime.of(LocalDate.parse("20200924", DateTimeFormatter.ofPattern("yyyyMMdd")), LocalTime.of(23, 59, 59));

        List<SettleTransMonthlySumRequest> batchList = transactionRepository.findByCreatedDate(fromDate, toDate);
        System.out.println("batchList.size(): " + batchList.size());
        System.out.println("batchList.toString(): ");
        System.out.println(batchList.toString());
    }

    @Test
    public void dateTimeUtilsTest() {
        LocalDateTime dateTime = LocalDateTime.now();
        //LocalDateTime dateTime = LocalDateTime.of(LocalDate.parse("20200922", DateTimeFormatter.ofPattern("yyyyMMdd")), LocalTime.of(0,0,0));

        LocalDateTime getMidnight = DateTimeUtils.getMidnight(dateTime);
        System.out.println("getMidnight: " + getMidnight);

        LocalDateTime getFirstDayOfMonth = DateTimeUtils.getFirstDayOfMonth(dateTime);
        System.out.println("getFirstDayOfMonth: " + getFirstDayOfMonth);

        LocalDateTime getFirstDayOfPrevMonth = DateTimeUtils.getFirstDayOfPrevMonth(dateTime);
        System.out.println("getFirstDayOfPrevMonth: " + getFirstDayOfPrevMonth);

        String stringDate = dateTime.format(DateTimeFormatter.ofPattern("yyyyMM"));
        System.out.println("stringDate: " + stringDate);
    }

}