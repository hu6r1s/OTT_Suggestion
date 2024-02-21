package com.three.ott_suggestion.comment;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

import com.three.ott_suggestion.comment.dto.CommentRequestDto;
import com.three.ott_suggestion.comment.dto.CommentResponseDto;
import com.three.ott_suggestion.comment.entity.Comment;
import com.three.ott_suggestion.comment.repository.CommentRepository;
import com.three.ott_suggestion.comment.service.CommentService;
import com.three.ott_suggestion.global.exception.InvalidInputException;
import com.three.ott_suggestion.global.exception.InvalidPostException;
import com.three.ott_suggestion.post.dto.PostRequestDto;
import com.three.ott_suggestion.post.dto.PostResponseDto;
import com.three.ott_suggestion.post.entity.Post;
import com.three.ott_suggestion.post.service.PostService;
import com.three.ott_suggestion.user.entity.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostService postService;

    @InjectMocks
    private CommentService commentService;

    private User user;
    private Post post;
    private Comment comment;

    private List<CommentResponseDto> mockCommentList = new ArrayList<>();

    @BeforeEach
    void setup() {
        String email = "test@test.com";
        String password = "ASDasd123!!!";
        String nickname = "test";
        String introduction = "test";
        user = new User(email, password, nickname, introduction, null, true);
        post = new Post(new PostRequestDto("test title", "test content"), user);
        comment = new Comment(new CommentRequestDto("test comment"), post, user);
    }

    void mockCommentSetup() {
        Comment comment1 = new Comment(new CommentRequestDto("comment1"), post, user);
        Comment comment2 = new Comment(new CommentRequestDto("comment2"), post, user);
        mockCommentList.add(new CommentResponseDto(comment1));
        mockCommentList.add(new CommentResponseDto(comment2));
    }

    @Test
    @DisplayName("댓글 작성")
    void 댓글_작성() throws Exception {
        // given
        String content = "test comment";

        CommentRequestDto requestDto = new CommentRequestDto(content);

        // when
        commentService.createComment(user, post.getId(), requestDto);

        // then
        assertDoesNotThrow(() -> true);
    }

    @Test
    @DisplayName("댓글 조회")
    void 댓글_조회() {
        //given
        mockCommentSetup();
        given(commentRepository.findAllByPostId(post.getId()))
            .willReturn(mockCommentList.stream().map(commentResponseDto -> {
                return new Comment(
                    new CommentRequestDto(commentResponseDto.getContent()
                    ), post, user);
            }).collect(Collectors.toList()));

        // when
        List<CommentResponseDto> response = commentService.getComments(post.getId());

        // then
        assertEquals(comment.getContent(), response.get(0).getContent());
    }

    @Test
    @DisplayName("게시글 수정")
    void 게시글_수정() throws IOException {
        // given
        mockCommentSetup();

        given(commentRepository.findById(comment.getId()))
            .willReturn(Optional.of(new Comment(
                new CommentRequestDto(
                    comment.getContent()
                ),
                post,
                user)));

        CommentRequestDto request = new CommentRequestDto("update comment");

        // when
        commentService.updateComment(
            user,
            post.getId(),
            comment.getId(),
            request
        );

        // then
        Optional<Comment> updateComment = commentRepository.findById(comment.getId());
        assertEquals(request.getContent(), updateComment.get().getContent());
    }

    @Test
    @DisplayName("게시글 수정 에러")
    void 게시글_수정_에러() {
        // given
        mockCommentSetup();

        given(commentRepository.findById(comment.getId()))
            .willReturn(Optional.empty());

        CommentRequestDto request = new CommentRequestDto("update comment");

        // when - then
        InvalidInputException e = assertThrows(InvalidInputException.class, () -> {
            commentService.updateComment(
                user,
                post.getId(),
                comment.getId(),
                request
            );
        });
        assertEquals("해당하는 댓글이 없습니다.", e.getMessage());
    }

    @Test
    @DisplayName("게시글 삭제")
    void 게시글_삭제() {
        // given
        mockCommentSetup();

        given(commentRepository.findById(comment.getId()))
            .willReturn(Optional.of(new Comment(
                new CommentRequestDto(
                    comment.getContent()
                ),
                post,
                user)));

        // when
        commentService.deleteComment(
            user,
            post.getId(),
            comment.getId()
        );

        // then
        Optional<Comment> updateComment = commentRepository.findById(comment.getId());
        assertNotNull(updateComment.get().getDeletedAt());
    }
}
