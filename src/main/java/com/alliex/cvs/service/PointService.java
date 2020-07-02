package com.alliex.cvs.service;

import com.alliex.cvs.domain.point.Point;
import com.alliex.cvs.domain.point.PointRepository;
import com.alliex.cvs.exception.PointLimitExcessException;
import com.alliex.cvs.web.dto.PointRequest;
import com.alliex.cvs.web.dto.PointResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PointService {

    private final PointRepository pointRepository;

    @Transactional
    public Long save(PointRequest pointRequest) {
        return pointRepository.save(pointRequest.toEntity()).getId();
    }

    @Transactional(readOnly = true)
    public PointResponse findById(Long id) {
        Point entity = pointRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found point. id: " + id));

        return new PointResponse(entity);
    }

    @Transactional
    public Long updatePointPlus(Long id, Double _point){

        Double originPoint = findById(id).getPoint();
        Double resultPoint = originPoint + _point;
        if(resultPoint > 500) { // 충전한도는 정해지는 대로 변경
            throw new PointLimitExcessException("The point is too much to charge. point : " + _point);
        }

        Point point = pointRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found point. id: " + id));

        point.setPoint(resultPoint);
        point.update(point.getPoint());

        return id;
    }

    @Transactional
    public Long updatePointMinus(Long id, Double _point) {
        Double originPoint = findById(id).getPoint();
        Double resultPoint = originPoint - _point;
        if(resultPoint < 0) {
            throw new PointLimitExcessException("The point is not enough to pay");
        }

        Point point = pointRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found point. id: " + id));

        point.setPoint(resultPoint);
        point.update(point.getPoint());

        return id;
    }

}
