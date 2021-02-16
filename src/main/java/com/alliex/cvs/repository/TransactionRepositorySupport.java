package com.alliex.cvs.repository;

import com.alliex.cvs.domain.type.TransactionType;
import com.alliex.cvs.entity.Transaction;
import com.alliex.cvs.web.dto.SettleTransMonthlySumRequest;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.alliex.cvs.entity.QTransaction.transaction;

@Repository
public class TransactionRepositorySupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory queryFactory;

    public TransactionRepositorySupport(JPAQueryFactory queryFactory) {
        super(Transaction.class);
        this.queryFactory = queryFactory;
    }

    public List<SettleTransMonthlySumRequest> getMonthlySum(LocalDateTime fromDate, LocalDateTime toDate) {
        return queryFactory
                .select(Projections.fields(SettleTransMonthlySumRequest.class,
                        transaction.user.id.as("userId"),
                        paymentCount().sum().as("approvalCount"),
                        paymentAmount().sum().as("approvalAmount"),
                        refundCount().sum().as("cancelCount"),
                        refundAmount().sum().as("cancelAmount")
                        )
                ).from(transaction)
                .where(betweenCreatedDate(fromDate, toDate))
                .groupBy(transaction.user.id)
                .fetch();

    }

    private NumberExpression<Integer> paymentCount() {
        return new CaseBuilder().when(transaction.type.eq(TransactionType.PAYMENT)).then(1).otherwise(0);
    }

    private NumberExpression<Long> paymentAmount() {
        return new CaseBuilder().when(transaction.type.eq(TransactionType.PAYMENT)).then(transaction.point).otherwise(0L);
    }

    private NumberExpression<Integer> refundCount() {
        return new CaseBuilder().when(transaction.type.eq(TransactionType.REFUND)).then(1).otherwise(0);
    }

    private NumberExpression<Long> refundAmount() {
        return new CaseBuilder().when(transaction.type.eq(TransactionType.REFUND)).then(transaction.point).otherwise(0L);
    }

    private BooleanExpression betweenCreatedDate(LocalDateTime fromDate, LocalDateTime toDate) {
        if (fromDate == null || toDate == null) {
            return null;
        }

        return transaction.createdDate.between(fromDate, toDate);
    }

}