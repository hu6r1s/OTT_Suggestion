package com.three.ott_suggestion.post.service;

import com.three.ott_suggestion.global.exception.InvalidPostException;
import com.three.ott_suggestion.post.dto.PostRequestDto;
import com.three.ott_suggestion.post.dto.PostResponseDto;
import com.three.ott_suggestion.post.entity.Post;
import com.three.ott_suggestion.post.repository.PostRepository;
import com.three.ott_suggestion.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public void createPost(PostRequestDto requestDto, User user) {
        Post post = new Post(requestDto, user);
        postRepository.save(post);
    }

    public List<PostResponseDto> getAllPosts() {
        return postRepository.findAll().stream().map(e -> new PostResponseDto(e, e.getUser())).toList();
    }

    public PostResponseDto getPost(Long postId) {
        Post post = findPost(postId);
       return new PostResponseDto(post);
    }


    public Post findPost(Long postId){
        return postRepository.findById(postId).orElseThrow(
            ()-> {
                String message = "해당 게시글이 없습니다.";
                return new InvalidPostException(message);
            }
        );
    }
}
