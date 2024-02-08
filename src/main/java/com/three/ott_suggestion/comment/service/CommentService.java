package com.three.ott_suggestion.comment.service;

import com.three.ott_suggestion.comment.dto.CommentRequestDto;
import com.three.ott_suggestion.comment.dto.CommentResponseDto;
import com.three.ott_suggestion.comment.entity.Comment;
import com.three.ott_suggestion.comment.repository.CommentRepository;
import com.three.ott_suggestion.global.exception.InvalidInputException;
import com.three.ott_suggestion.post.entity.Post;
import com.three.ott_suggestion.post.repository.PostRepository;
import com.three.ott_suggestion.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Transactional
    public void createComment(User user, Long postId, CommentRequestDto requestDto) {
        Post post = postRepository.findById(postId).orElseThrow(
            () -> new InvalidInputException("해당하는 할 일이 없습니다.")
        );

        Comment comment = new Comment(requestDto, post, user);
        commentRepository.save(comment);
    }
}
