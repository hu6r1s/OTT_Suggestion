package com.three.ott_suggestion.comment.service;

import com.three.ott_suggestion.comment.dto.CommentRequestDto;
import com.three.ott_suggestion.comment.dto.CommentResponseDto;
import com.three.ott_suggestion.comment.entity.Comment;
import com.three.ott_suggestion.comment.repository.CommentRepository;
import com.three.ott_suggestion.global.exception.AuthenticationException;
import com.three.ott_suggestion.global.exception.InvalidInputException;
import com.three.ott_suggestion.post.entity.Post;
import com.three.ott_suggestion.post.repository.PostRepository;
import com.three.ott_suggestion.user.entity.User;
import java.util.List;
import java.util.Objects;
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
        Post post = findPost(postId);

        Comment comment = new Comment(requestDto, post, user);
        commentRepository.save(comment);
    }

    public List<CommentResponseDto> getComments(Long postId) {
        findPost(postId);
        List<Comment> comments = commentRepository.findAllByPostId(postId);
        return comments.stream()
            .map(CommentResponseDto::new)
            .toList();
    }

    @Transactional
    public void updateComment(User user,
        Long postId,
        Long commentId,
        CommentRequestDto requestDto) {
        findPost(postId);
        Comment comment = findComment(commentId);

        validate(comment.getUser().getId(), user.getId());

        comment.update(requestDto);
    }

    @Transactional
    public void deleteComment(User user, Long postId, Long commentId) {
        findPost(postId);
        Comment comment = findComment(commentId);

        validate(comment.getUser().getId(), user.getId());
        comment.softDelete();
    }

    private Post findPost(Long postId) {
        return postRepository.findById(postId).orElseThrow(
            () -> new InvalidInputException("해당하는 할 일이 없습니다.")
        );
    }

    private Comment findComment(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
            () -> new InvalidInputException("해당하는 댓글이 없습니다.")
        );
    }

    private void validate(Long originId, Long inputId) {
        if (!Objects.equals(originId, inputId)) {
            throw new AuthenticationException("작성자가 다릅니다");
        }
    }
}
