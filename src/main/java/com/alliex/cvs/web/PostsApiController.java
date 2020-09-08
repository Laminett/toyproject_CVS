package com.alliex.cvs.web;

import com.alliex.cvs.service.PostsService;
import com.alliex.cvs.web.dto.PostsListResponse;
import com.alliex.cvs.web.dto.PostsResponse;
import com.alliex.cvs.web.dto.PostsSaveRequest;
import com.alliex.cvs.web.dto.PostsUpdateRequest;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class PostsApiController {

    private final PostsService postsService;

    @ApiOperation(value = "savePosts", notes = "예제입니다.")
    @PostMapping("/api/v1/posts")
    public Long save(@RequestBody PostsSaveRequest postsSaveRequest) {
        return postsService.save(postsSaveRequest);
    }

    @PutMapping("/api/v1/posts/{id}")
    public Long update(@PathVariable Long id, @RequestBody PostsUpdateRequest request) {
        return postsService.update(id, request);
    }

    @GetMapping("/api/v1/posts")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "authorization header", required = true, dataType = "string", paramType = "header", defaultValue = "Bearer xxx")
    })
    public List<PostsListResponse> getPosts() {
        return postsService.findAllDesc();
    }

    @GetMapping("/api/v1/posts/{id}")
    public PostsResponse findById(@PathVariable Long id) {
        return postsService.findById(id);
    }

    @DeleteMapping("/api/v1/posts/{id}")
    public Long delete(@PathVariable Long id) {
        postsService.delete(id);

        return id;
    }

}
