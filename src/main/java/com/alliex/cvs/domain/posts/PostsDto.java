package com.alliex.cvs.domain.posts;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PostsDto {

    private String title;

    private Long articleCount;

}
