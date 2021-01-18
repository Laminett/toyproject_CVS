package com.alliex.cvs.service;

import com.alliex.cvs.web.dto.PointHistorySaveRequest;
import com.alliex.cvs.web.dto.PointHistorySaveResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

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
        Long point = 230L;

        PointHistorySaveRequest pointHistorySaveRequest = PointHistorySaveRequest.builder()
                .userId(userId)
                .point(point)
                .build();

        PointHistorySaveResponse result = pointHistoryService.save(pointHistorySaveRequest);
        // Get Point History.
        assertThat(result.getUserId()).isEqualTo(400);
        assertThat(result.getPoint()).isEqualTo(230);
    }

}