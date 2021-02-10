package com.alliex.cvs.repository;

import com.alliex.cvs.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.alliex.cvs.entity.QPoint.point1;
import static com.alliex.cvs.entity.QUser.user;


@Repository
public class UserRepositorySupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory queryFactory;

    public UserRepositorySupport(JPAQueryFactory queryFactory) {
        super(User.class);
        this.queryFactory = queryFactory;
    }


    public List<User> findAllWithPoint(Pageable pageable) {
        return queryFactory
                .selectFrom(user)
                .join(user.point, point1).fetchJoin()
                .fetch();
    }

}
