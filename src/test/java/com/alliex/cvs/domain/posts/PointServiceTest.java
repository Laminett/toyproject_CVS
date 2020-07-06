package com.alliex.cvs.domain.posts;

import com.alliex.cvs.domain.point.Point;
import com.alliex.cvs.domain.point.PointHistory;
import com.alliex.cvs.domain.point.PointHistoryRepository;
import com.alliex.cvs.domain.point.PointRepository;
import com.alliex.cvs.service.PointHistoryService;
import com.alliex.cvs.service.PointService;
import com.alliex.cvs.web.dto.PointHistoryResponse;
import com.alliex.cvs.web.dto.PointResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PointServiceTest {

    @Autowired
    PointRepository pointRepository;

    @Autowired
    PointHistoryRepository pointHistoryRepository;

    @Autowired
    PointService pointService;

    @Autowired
    PointHistoryService pointHistoryService;

    @Test
    public void insertPointHistory() {
        long userId = 2L;
        int point = 110;
        String registrant = "tester";

        pointHistoryRepository.save(PointHistory.builder()
                .userId(userId)
                .point(point)
                .registrant(registrant)
                .build());

        List<PointHistoryResponse> pointHistoryResponseList = pointHistoryService.findByUserId(userId);
        PointHistoryResponse pointHistoryResponse = pointHistoryResponseList.get(0);
        assertThat(pointHistoryResponse.getUserId()).isEqualTo(userId);
        assertThat(pointHistoryResponse.getPoint()).isEqualTo(point);
    }

    @Test
    public void insertPoint() {
        long userId = 2L;
        int point = 120;

        pointRepository.save(Point.builder()
            .userId(userId)
            .point(point)
            .build());

        PointResponse pointResponse = pointService.findByUserId(userId);
        assertThat(pointResponse.getUserId()).isEqualTo(userId);
        assertThat(pointResponse.getPoint()).isEqualTo(point);
    }

    @Test
    public void updatePointPlus() {
        long userId = 1L;
        int temp_point = 30;

        long result =  pointService.updatePointPlus(userId, temp_point);
        assertThat(result).isEqualTo(userId);
    }

    @Test
    public void updatePointMinus() {
        long userId = 1L;
        int temp_point = 30;

        long result = pointService.updatePointMinus(userId, temp_point);
        assertThat(result).isEqualTo(userId);
    }

}