package com.alliex.cvs.domain.posts;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional()
@Rollback()
public class PostsRepositoryTest {

    @Autowired
    PostsRepository postsRepository;

    @Test
    public void getPosts() {
        // given
        String title = "test title";
        String content = "test content";

        postsRepository.save(Posts.builder()
                .title(title)
                .content(content)
                .author("author")
                .build());

        // when
        List<Posts> postsList = postsRepository.findAll();

        // then
        Posts posts = postsList.get(0);
        assertThat(posts.getTitle()).isEqualTo(title);
        assertThat(posts.getContent()).isEqualTo(content);
    }

    @Test
    public void getPostsDto() {
        // given
        postsRepository.save(Posts.builder()
                .title("title1")
                .content("myContent")
                .author("author")
                .build());

        postsRepository.save(Posts.builder()
                .title("title1")
                .content("myContent")
                .author("author")
                .build());

        postsRepository.save(Posts.builder()
                .title("title2")
                .content("myContent")
                .author("author")
                .build());


        // when
        List<PostsDto> postsList = postsRepository.findAllPostsByContent("myContent");

        // then
        PostsDto postsDto = postsList.get(0);
        assertThat(postsDto.getTitle()).isEqualTo("title1");
        assertThat(postsDto.getArticleCount()).isEqualTo(2);

        PostsDto postsDto2 = postsList.get(1);
        assertThat(postsDto2.getTitle()).isEqualTo("title2");
        assertThat(postsDto2.getArticleCount()).isEqualTo(1);
    }

    @Test
    public void baseTimeEntity() {
        // given
        LocalDateTime now = LocalDateTime.of(2020, 3, 11, 0, 0, 0);
        postsRepository.save(Posts.builder()
                .title("title")
                .content("content")
                .author("author")
                .build());

        // when
        List<Posts> postsList = postsRepository.findAll();
        System.out.println("postsList:" + postsList);

        // then
        Posts posts = postsList.get(0);
        System.out.println("posts:" + posts);

        System.out.printf("createDate:" + posts.getCreatedDate() + ", modifiedDate:" + posts.getModifiedDate());
        assertThat(posts.getCreatedDate()).isAfter(now);
        assertThat(posts.getModifiedDate()).isAfter(now);
    }

}
