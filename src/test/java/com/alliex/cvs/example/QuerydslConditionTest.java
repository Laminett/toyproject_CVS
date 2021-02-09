package com.alliex.cvs.example;


import com.alliex.cvs.domain.Posts;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.alliex.cvs.domain.posts.QPosts.posts;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class QuerydslConditionTest {

    @Autowired
    EntityManager em;

    JPAQueryFactory queryFactory;

    @BeforeEach
    public void before() {
        queryFactory = new JPAQueryFactory(em);

        Posts posts1 = new Posts("posts1", "content1");
        Posts posts2 = new Posts("posts2", "content2");
        em.persist(posts1);
        em.persist(posts2);
    }

    @Test
    public void jpql() {
        Posts findPosts = em.createQuery("select p from Posts p where p.title = :title", Posts.class)
                .setParameter("title", "posts1")
                .getSingleResult();

        assertThat(findPosts.getTitle()).isEqualTo("posts1");
    }

    @Test
    public void querydsl_fetchOne() {
        Posts findPosts;

        // single condition
        findPosts = queryFactory
                .selectFrom(posts)
                .where(posts.title.eq("posts1"))
                .fetchOne();

        assertThat(findPosts.getTitle()).isEqualTo("posts1");

        // multiple condition
        findPosts = queryFactory
                .selectFrom(posts)
                .where(
                        posts.title.eq("posts2"),
                        posts.content.eq("content2")
                )
                .fetchOne();

        assertThat(findPosts.getTitle()).isEqualTo("posts2");
        assertThat(findPosts.getContent()).isEqualTo("content2");
    }

    @Test
    public void querydsl_fetch() {
        List<Posts> postsList = queryFactory
                .selectFrom(posts)
                .fetch();

        for (Posts item : postsList) {
            System.out.println("item = " + item);
        }
    }

    @Test
    public void querydsl_fetchFirst() {
        Posts findPosts = queryFactory
                .selectFrom(posts)
                .fetchFirst();

        System.out.println("findPosts = " + findPosts);
    }

    @Test
    public void querydsl_fetchResults() {
        QueryResults<Posts> fetchResults = queryFactory
                .selectFrom(posts)
                .fetchResults();

        System.out.println("fetchResults.getTotal(): " + fetchResults.getTotal());
        System.out.println("fetchResults.getResults(): " + fetchResults.getResults());
        System.out.println("fetchResults.getLimit(): " + fetchResults.getLimit());
        System.out.println("fetchResults.getOffset(): " + fetchResults.getOffset());
    }

    @Test
    public void querydsl_fetchCount() {
        long count = queryFactory
                .selectFrom(posts)
                .fetchCount();

        System.out.println("count = " + count);
    }

    @Test
    public void sort() {
        em.persist(new Posts("posts100", "content100"));
        em.persist(new Posts("posts101", "content101"));
        em.persist(new Posts("posts102", "content102"));

        List<Posts> result = queryFactory
                .selectFrom(posts)
                .where(posts.title.startsWith("posts10"))
                .orderBy(posts.title.desc(), posts.content.asc().nullsLast())
                .fetch();

        Posts posts102 = result.get(0);
        Posts posts101 = result.get(1);
        Posts posts100 = result.get(2);

        assertThat(posts102.getTitle()).isEqualTo("posts102");
        assertThat(posts101.getTitle()).isEqualTo("posts101");
        assertThat(posts100.getTitle()).isEqualTo("posts100");
    }

}
