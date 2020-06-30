package com.alliex.cvs.web;

import com.alliex.cvs.service.PostsService;
import com.alliex.cvs.web.dto.PostsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RequiredArgsConstructor
@Controller
public class PostsController {

    private final PostsService postsService;

    @GetMapping("/posts")
    public String getPosts() {
        return "posts";
    }

    @GetMapping("/posts/save")
    public String postsSave() {
        return "posts-save";
    }

    @GetMapping("/posts/update/{id}")
    public String postsUpdate(@PathVariable("id") Long id, Model model) {
        PostsResponse postsResponse = postsService.findById(id);
        model.addAttribute("post", postsResponse);

        return "posts-update";
    }

}
