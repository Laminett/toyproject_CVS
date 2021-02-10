package com.alliex.cvs.domain.point;

import com.alliex.cvs.web.dto.PointHistoryRequest;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

import static com.alliex.cvs.domain.point.QPointHistory.pointHistory;

@Repository
public class PointHistorySupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory queryFactory;

    public PointHistorySupport(JPAQueryFactory queryFactory) {
        super(PointHistory.class);
        this.queryFactory = queryFactory;
    }

    public Page<PointHistory> getPointHistories(Pageable pageable, PointHistoryRequest pointHistoryRequest) {
        QueryResults<PointHistory> pointHistories = queryFactory
                .selectFrom(pointHistory)
                .where(
                        betweenCreatedDate(pointHistoryRequest.getStartDate(), pointHistoryRequest.getEndDate()),
                        likeFullName(pointHistoryRequest.getFullName()),
                        eqStatus(pointHistoryRequest.getStatus())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        return new PageImpl<>(pointHistories.getResults(), pageable, pointHistories.getTotal());
    }

    private BooleanExpression betweenCreatedDate(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            return null;
        }
        return pointHistory.createdDate.between(startDate.atStartOfDay(), endDate.atTime(23, 59, 59));
    }

    private BooleanExpression likeFullName(String fullname) {
        if (StringUtils.isBlank(fullname)) {
            return null;
        }
        return pointHistory.user.fullName.like(fullname + "%");
    }

    private BooleanExpression eqStatus(String status) {
        if (StringUtils.isBlank(status)) {
            return null;
        }
        return pointHistory.status.eq(status);
    }

}