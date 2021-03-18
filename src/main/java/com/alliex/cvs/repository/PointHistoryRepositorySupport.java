package com.alliex.cvs.repository;

import com.alliex.cvs.domain.type.PointHistoryStatus;
import com.alliex.cvs.entity.PointHistory;
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

import static com.alliex.cvs.entity.QPointHistory.pointHistory;

@Repository
public class PointHistoryRepositorySupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory queryFactory;

    public PointHistoryRepositorySupport(JPAQueryFactory queryFactory) {
        super(PointHistory.class);
        this.queryFactory = queryFactory;
    }

    public Page<PointHistory> getPointHistories(Pageable pageable, PointHistoryRequest pointHistoryRequest) {
        QueryResults<PointHistory> pointHistories = queryFactory
                .selectFrom(pointHistory)
                .where(
                        betweenCreatedDate(pointHistoryRequest.getStartDate(), pointHistoryRequest.getEndDate()),
                        containsFullName(pointHistoryRequest.getFullName()),
                        eqStatus(pointHistoryRequest.getStatus())
                )
                .orderBy(pointHistory.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        return new PageImpl<>(pointHistories.getResults(), pageable, pointHistories.getTotal());
    }

    private BooleanExpression betweenCreatedDate(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            return null;
        }

        return pointHistory.createdAt.between(startDate.atStartOfDay(), endDate.atTime(23, 59, 59));
    }

    private BooleanExpression containsFullName(String fullName) {
        if (StringUtils.isBlank(fullName)) {
            return null;
        }

        return pointHistory.user.fullName.contains(fullName);
    }

    private BooleanExpression eqStatus(String status) {
        if (StringUtils.isBlank(status)) {
            return null;
        }

        return pointHistory.status.eq(PointHistoryStatus.valueOf(status));
    }

}