package com.alliex.cvs.service;

import com.alliex.cvs.domain.posts.Posts;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional()
public class PostsServiceTest {

    @Autowired
    PostsService postsService;

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
    public void getPostsByTitle() {
        List<Posts> postsList = postsService.getPostsByTitle("posts1");
        for (Posts posts : postsList) {
            System.out.println("posts = " + posts);
        }

        assertThat(postsList)
                .extracting("title")
                .containsExactly("posts1");
    }

}