package com.alliex.cvs.service;

import com.alliex.cvs.domain.point.Point;
import com.alliex.cvs.domain.point.PointRepository;
import com.alliex.cvs.exception.PointLimitExcessException;
import com.alliex.cvs.web.dto.PointResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PointService {

    private final PointRepository pointRepository;

    @Transactional
    public void save(Long userId, Long point) {
        Point _point = Point.builder()
                .userId(userId)
                .point(point)
                .build();

        pointRepository.save(_point);
    }

    @Transactional(readOnly = true)
    public PointResponse findByUserId(Long userId) {
        Point entity = pointRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Not found point data. user id : " + userId));
        return new PointResponse(entity);
    }

    @Transactional
    public Long updatePointPlus(Long userId, Long point) {
        Point pointEntity = pointRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Not found point data. user id : " + userId));

        Long originPoint = pointEntity.getPoint();
        Long resultPoint = originPoint + point;

        // 충전한도는 정해지는 대로 변경
        if (resultPoint > 2000000) {
            throw new PointLimitExcessException(point);
        }

        pointEntity.setPoint(resultPoint);
        pointEntity.update(pointEntity.getPoint());

        return userId;
    }

    @Transactional
    public Long updatePointMinus(Long userId, Long point) {
        Point pointEntity = pointRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Not found point data. user id : " + userId));

        Long originPoint = pointEntity.getPoint();
        Long resultPoint = originPoint - point;

        if (resultPoint < 0) {
            throw new PointLimitExcessException(point);
        }

        pointEntity.setPoint(resultPoint);
        pointEntity.update(pointEntity.getPoint());

        return userId;
    }

}