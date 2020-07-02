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

        long id = 4L;
        double point = 130.00;
        String registrant = "tester";

        pointHistoryRepository.save(PointHistory.builder()
                .id(id)
                .point(point)
                .registrant(registrant)
                .build());

        PointHistoryResponse pointHistoryResponse = pointHistoryService.findById(id);

        assertThat(pointHistoryResponse.getId()).isEqualTo(id);
        assertThat(pointHistoryResponse.getPoint()).isEqualTo(point);
        assertThat(pointHistoryResponse.getRegistrant()).isEqualTo(registrant);
    }

    @Test
    public void insertPoint() {

        long id = 4L;
        double point = 130.00;

        pointRepository.save(Point.builder()
                .id(id)
                .point(point)
                .build());

        PointResponse pointResponse = pointService.findById(id);
        assertThat(pointResponse.getId()).isEqualTo(id);
        assertThat(pointResponse.getPoint()).isEqualTo(point);
    }

    @Test
    public void updatePointPlus() {
        long id = 1L;
        double temp_point = 30.00;

        long result =  pointService.updatePointPlus(id, temp_point);

        assertThat(result).isEqualTo(id);
    }

    @Test
    public void updatePointMinus() {
        long id = 1L;
        double temp_point = 30.00;

        long result = pointService.updatePointMinus(id, temp_point);

        assertThat(result).isEqualTo(id);
    }
}