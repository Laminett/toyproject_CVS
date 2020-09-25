package com.alliex.cvs.service;

import com.alliex.cvs.web.dto.PointHistorySaveRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@Transactional()
@Rollback()
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:test-data.sql"),
})
public class PointHistoryServiceTest {

    @Autowired
    PointHistoryService pointHistoryService;

    @Test
    public void savePointHistory() {
        long userId = 400L;
        int point = 230;

        PointHistorySaveRequest pointHistorySaveRequest = PointHistorySaveRequest.builder()
                .userId(userId)
                .point(point)
                .build();

        Long result = pointHistoryService.save(pointHistorySaveRequest);
        System.out.println("result : " + result);
    }

}