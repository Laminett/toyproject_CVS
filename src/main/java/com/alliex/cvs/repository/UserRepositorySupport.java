package com.alliex.cvs.repository;

import com.alliex.cvs.entity.User;
import com.alliex.cvs.web.dto.UserRequest;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import static com.alliex.cvs.entity.QPoint.point1;
import static com.alliex.cvs.entity.QUser.user;


@Repository
public class UserRepositorySupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory queryFactory;

    public UserRepositorySupport(JPAQueryFactory queryFactory) {
        super(User.class);
        this.queryFactory = queryFactory;
    }

    public Page<User> findAll(Pageable pageable, UserRequest userRequest) {
        QueryResults<User> results = getQuerydsl().applyPagination(pageable, queryFactory
                .selectFrom(user)
                .join(user.point, point1).fetchJoin()
                .where(
                        usernameEq(userRequest.getUsername()),
                        fullNameLike(userRequest.getFullName()),
                        emailLike(userRequest.getEmail()),
                        departmentEq(userRequest.getDepartment())

                )).fetchResults();

        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }

    private Predicate usernameEq(String username) {
        return StringUtils.isNotBlank(username) ? user.username.eq(username) : null;
    }

    private Predicate fullNameLike(String fullName) {
        return StringUtils.isNotBlank(fullName) ? user.fullName.contains(fullName) : null;
    }

    private Predicate emailLike(String email) {
        return StringUtils.isNotBlank(email) ? user.email.contains(email) : null;
    }

    private BooleanExpression departmentEq(String department) {
        return StringUtils.isNotBlank(department) ? user.department.eq(department) : null;
    }

}
