package com.three.ott_suggestion.post.service;

import com.three.ott_suggestion.post.dto.PostRequestDto;
import com.three.ott_suggestion.post.entity.Post;
import com.three.ott_suggestion.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public void createPost(PostRequestDto requestDto) {
        Post post = new Post(requestDto);
//        postRepository.save(post);
    }
}
