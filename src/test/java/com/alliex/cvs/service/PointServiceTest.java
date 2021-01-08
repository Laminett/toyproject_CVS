package com.alliex.cvs.service;

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
public class PointServiceTest {

    @Autowired
    PointService pointService;

    private final Long testId = 400L;

    @Test
    public void updatePointPlus() {
        long result = pointService.updatePointPlus(testId, 30L);
        assertThat(result).isEqualTo(testId);
    }

    @Test
    public void updatePointMinus() {
        long result = pointService.updatePointMinus(testId, 30L);
        assertThat(result).isEqualTo(testId);
    }

}