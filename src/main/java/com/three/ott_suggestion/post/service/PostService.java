package com.three.ott_suggestion.post.service;

import com.three.ott_suggestion.global.exception.InvalidPostException;
import com.three.ott_suggestion.post.dto.PostRequestDto;
import com.three.ott_suggestion.post.dto.PostResponseDto;
import com.three.ott_suggestion.post.entity.Post;
import com.three.ott_suggestion.post.repository.PostRepository;
import com.three.ott_suggestion.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j(topic = "service")
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public void createPost(PostRequestDto requestDto, User user) {
        log.info(user.getEmail());
        Post post = new Post(requestDto, user);
        postRepository.save(post);
    }

    public List<PostResponseDto> getAllPosts() {
        return postRepository.findAll().stream().map(PostResponseDto::new)
            .toList();
    }

    public PostResponseDto getPost(Long postId) {
        Post post = findPost(postId);
        return new PostResponseDto(post);
    }


    public Post findPost(Long postId) {
        return postRepository.findById(postId).orElseThrow(
            () -> {
                String message = "해당 게시글이 없습니다.";
                return new InvalidPostException(message);
            }
        );
    }

    @Transactional
    public PostResponseDto updatePost(Long postId, PostRequestDto requestDto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() ->
                    new InvalidPostException("해당 게시글이 없습니다."));
        post.update(requestDto);
        return new PostResponseDto(post);
    }
}
