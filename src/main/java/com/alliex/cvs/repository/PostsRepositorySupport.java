package com.alliex.cvs.repository;

import com.alliex.cvs.entity.Posts;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.alliex.cvs.entity.QPosts.posts;


@Repository
public class PostsRepositorySupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory queryFactory;

    public PostsRepositorySupport(JPAQueryFactory queryFactory) {
        super(Posts.class);
        this.queryFactory = queryFactory;
    }

    public List<Posts> findByTitle(String title) {
        return queryFactory
                .selectFrom(posts)
                .where(posts.title.eq(title))
                .fetch();
    }

}
