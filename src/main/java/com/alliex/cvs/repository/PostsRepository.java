package com.alliex.cvs.repository;

import com.alliex.cvs.entity.Posts;
import com.alliex.cvs.web.dto.PostsDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostsRepository extends JpaRepository<Posts, Long> {

    @Query("select p from Posts p order by p.id desc")
    List<Posts> findAllDesc();

    @Query("select new com.alliex.cvs.web.dto.PostsDto(" +
            " p.title," +
            " count(p.title)" +
            ")" +
            " from Posts p" +
            " where p.content = :content" +
            " group by p.title" +
            " order by p.title")
    List<PostsDto> findAllPostsByContent(@Param("content") String content);

}
