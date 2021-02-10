package com.alliex.cvs.domain.settle;

import com.alliex.cvs.web.dto.SettleRequest;
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

import static com.alliex.cvs.domain.settle.QSettle.settle;

@Repository
public class SettleSupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory queryFactory;

    public SettleSupport(JPAQueryFactory queryFactory) {
        super(Settle.class);
        this.queryFactory = queryFactory;
    }

    public Page<Settle> getSettles(Pageable pageable, SettleRequest settleRequest) {
        QueryResults<Settle> settles = queryFactory
                .selectFrom(settle)
                .where(
                        eqAggregatedAt(settleRequest.getAggregatedAt()),
                        likeFullName(settleRequest.getFullName())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        return new PageImpl<>(settles.getResults(), pageable, settles.getTotal());
    }

    private BooleanExpression eqAggregatedAt(LocalDate aggregatedAt) {
        if (aggregatedAt == null) {
            return null;
        }
        return settle.aggregatedAt.eq(aggregatedAt);
    }

    private BooleanExpression likeFullName(String fullName) {
        if (StringUtils.isBlank(fullName)) {
            return null;
        }
        return settle.user.fullName.like(fullName + "%");
    }

}