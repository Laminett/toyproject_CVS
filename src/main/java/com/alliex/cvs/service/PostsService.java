package com.alliex.cvs.service;

import com.alliex.cvs.entity.Posts;
import com.alliex.cvs.repository.PostsRepository;
import com.alliex.cvs.repository.PostsRepositorySupport;
import com.alliex.cvs.web.dto.PostsListResponse;
import com.alliex.cvs.web.dto.PostsResponse;
import com.alliex.cvs.web.dto.PostsSaveRequest;
import com.alliex.cvs.web.dto.PostsUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostsService {

    private final PostsRepository postsRepository;

    private final PostsRepositorySupport postsRepositorySupport;

    @Transactional
    public Long save(PostsSaveRequest postsSaveRequest) {
        return postsRepository.save(postsSaveRequest.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequest request) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found posts. id: " + id));

        posts.update(request.getTitle(), request.getContent());

        return id;
    }

    @Transactional
    public void delete(Long id) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found posts. id:" + id));

        postsRepository.delete(posts);
    }

    @Transactional(readOnly = true)
    public PostsResponse findById(Long id) {
        Posts entity = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found posts. id: " + id));

        return new PostsResponse(entity);
    }

    @Transactional(readOnly = true)
    public List<PostsListResponse> findAllDesc() {
        return postsRepository.findAllDesc().stream()
                .map(PostsListResponse::new)
                .collect(Collectors.toList());
    }

    public List<Posts> getPostsByTitle(String title) {
        return postsRepositorySupport.findByTitle(title);
    }

}
