package com.alliex.cvs.web.dto;

import com.alliex.cvs.domain.Posts;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PostsResponse {

    private Long id;

    private String title;

    private String content;

    private String author;

    @Builder
    public PostsResponse(Posts entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.author = entity.getAuthor();
    }

}
