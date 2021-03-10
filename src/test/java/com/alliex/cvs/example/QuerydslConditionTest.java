package com.alliex.cvs.example;


import com.alliex.cvs.domain.type.Role;
import com.alliex.cvs.domain.type.UserStatus;
import com.alliex.cvs.entity.User;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.alliex.cvs.entity.QUser.user;
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

        em.persist(User.builder()
                .username("testUsername1")
                .password("testPassword1")
                .department("department1")
                .fullName("fullName1")
                .email("email@email.com1")
                .phoneNumber("phoneNumber1")
                .status(UserStatus.ACTIVE)
                .role(Role.USER)
                .build());

        em.persist(User.builder()
                .username("testUsername2")
                .password("testPassword2")
                .department("department2")
                .fullName("fullName2")
                .email("email@email.com2")
                .phoneNumber("phoneNumber2")
                .status(UserStatus.ACTIVE)
                .role(Role.USER)
                .build());
    }

    @Test
    public void jpql() {
        User user = em.createQuery("select u from User u where u.username = :username", User.class)
                .setParameter("username", "testUsername1")
                .getSingleResult();

        assertThat(user.getUsername()).isEqualTo("testUsername1");
    }

    @Test
    public void querydsl_fetchOne() {
        User findUser;

        // single condition
        findUser = queryFactory
                .selectFrom(user)
                .where(user.username.eq("testUsername1"))
                .fetchOne();

        assertThat(findUser.getUsername()).isEqualTo("testUsername1");

        // multiple condition
        findUser = queryFactory
                .selectFrom(user)
                .where(
                        user.username.eq("testUsername2"),
                        user.department.eq("department2")
                )
                .fetchOne();

        assertThat(findUser.getUsername()).isEqualTo("testUsername2");
        assertThat(findUser.getDepartment()).isEqualTo("department2");
    }

    @Test
    public void querydsl_fetch() {
        List<User> users = queryFactory
                .selectFrom(user)
                .fetch();

        users.forEach(item -> System.out.println(item));
    }

    @Test
    public void querydsl_fetchFirst() {
        User findUser = queryFactory
                .selectFrom(user)
                .fetchFirst();

        System.out.println("findPosts = " + findUser);
    }

    @Test
    public void querydsl_fetchResults() {
        QueryResults<User> fetchResults = queryFactory
                .selectFrom(user)
                .fetchResults();

        System.out.println("fetchResults.getTotal(): " + fetchResults.getTotal());
        System.out.println("fetchResults.getResults(): " + fetchResults.getResults());
        System.out.println("fetchResults.getLimit(): " + fetchResults.getLimit());
        System.out.println("fetchResults.getOffset(): " + fetchResults.getOffset());
    }

    @Test
    public void querydsl_fetchCount() {
        long count = queryFactory
                .selectFrom(user)
                .fetchCount();

        System.out.println("count = " + count);
    }

    @Test
    public void sort() {
        List<User> result = queryFactory
                .selectFrom(user)
                .where(user.username.startsWith("testUsername"))
                .orderBy(user.username.desc(), user.department.asc().nullsLast())
                .fetch();

        User user1 = result.get(0);
        User user2 = result.get(1);

        assertThat(user1.getUsername()).isEqualTo("testUsername2");
        assertThat(user2.getUsername()).isEqualTo("testUsername1");
    }

}
