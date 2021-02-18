package com.alliex.cvs.repository;

import com.alliex.cvs.domain.type.PaymentType;
import com.alliex.cvs.domain.type.TransactionState;
import com.alliex.cvs.domain.type.TransactionType;
import com.alliex.cvs.entity.Transaction;
import com.alliex.cvs.web.dto.SettleTransMonthlySumRequest;
import com.alliex.cvs.web.dto.TransactionRequest;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static com.alliex.cvs.entity.QTransaction.transaction;

@Repository
public class TransactionRepositorySupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory queryFactory;

    public TransactionRepositorySupport(JPAQueryFactory queryFactory) {
        super(Transaction.class);
        this.queryFactory = queryFactory;
    }

    public Page<Transaction> findBySearchValueWithDate(Pageable pageable, TransactionRequest transactionRequest) {
        JPQLQuery<Transaction> results = queryFactory
                .selectFrom(transaction)
                .where(usernameEq(transactionRequest.getUserId()), paymentTypeEq(transactionRequest.getPaymentType()),
                        pointEq(transactionRequest.getPoint()), stateEq(transactionRequest.getState()),
                        typeEq(transactionRequest.getType())
                        , betweenCreateDate(transactionRequest.getFromDate(), transactionRequest.getToDate()));
        JPQLQuery<Transaction> rere = getQuerydsl().applyPagination(pageable,results);
        Long totalCount = results.fetchCount();
        return new PageImpl<>(rere.fetchResults().getResults(), pageable, totalCount);
    }

    private BooleanExpression usernameEq(String username) {
        return StringUtils.isNotBlank(username) ? transaction.user.username.eq(username) : null;
    }

    private BooleanExpression paymentTypeEq(String paymentType) {
        return StringUtils.isNotBlank(paymentType) ? transaction.paymentType.eq(PaymentType.valueOf(paymentType.toUpperCase())) : null;
    }

    private BooleanExpression pointEq(Long point) {
        return point != null ? transaction.point.like(String.valueOf(point)) : null;
    }

    private BooleanExpression stateEq(String state) {
        return StringUtils.isNotBlank(state) ? transaction.state.eq(TransactionState.valueOf(state.toUpperCase())) : null;
    }

    private BooleanExpression typeEq(String type) {
        return StringUtils.isNotBlank(type) ? transaction.type.eq(TransactionType.valueOf(type.toUpperCase())) : null;
    }

    private BooleanExpression betweenCreateDate(LocalDate fromDate, LocalDate toDate) {
        if (fromDate == null || toDate == null) {
            return null;
        }

        LocalDateTime fromDateTime = LocalDateTime.of(fromDate, LocalTime.of(0, 0, 0));
        LocalDateTime toDateTime = LocalDateTime.of(toDate, LocalTime.of(23, 59, 59));

        return transaction.createdDate.between(fromDateTime, toDateTime);
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