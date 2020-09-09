package com.alliex.cvs.domain.posts;

import com.alliex.cvs.domain.point.PointHistoryRepository;
import com.alliex.cvs.service.PointHistoryService;
import com.alliex.cvs.web.dto.PointHistorySaveRequest;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PointHistoryServiceTest {

    @Autowired
    PointHistoryRepository pointHistoryRepository;

    @Autowired
    PointHistoryService pointHistoryService;

    @Test
    public void savePointHistory() {
        long userId = 3L;
        int point = 230;

        PointHistorySaveRequest pointHistorySaveRequest = PointHistorySaveRequest.builder()
                .userId(userId)
                .point(point)
                .build();

        Long result = pointHistoryService.save(pointHistorySaveRequest);
        System.out.println("result : " + result);
    }

}