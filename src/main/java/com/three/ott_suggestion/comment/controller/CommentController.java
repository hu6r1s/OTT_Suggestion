package com.three.ott_suggestion.comment.controller;

import com.three.ott_suggestion.comment.dto.CommentRequestDto;
import com.three.ott_suggestion.comment.dto.CommentResponseDto;
import com.three.ott_suggestion.comment.service.CommentService;
import com.three.ott_suggestion.global.response.CommonResponse;
import com.three.ott_suggestion.global.util.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@Tag(name = "Comment", description = "댓글 컨트롤러")
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글 조회", description = "게시글에 대한 댓글들을 조회할 수 있는 API")
    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<CommonResponse<List<CommentResponseDto>>> getComments(
        @PathVariable Long postId) {
        List<CommentResponseDto> responseDtos = commentService.getComments(postId);
        return ResponseEntity.status(HttpStatus.OK.value()).body(
            CommonResponse.<List<CommentResponseDto>>builder().data(responseDtos).build());
    }

    @Operation(summary = "댓글 생성", description = "댓글을 작성할 수 있는 API")
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommonResponse<Void>> createComment(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable Long postId,
        @RequestBody CommentRequestDto requestDto) {
        commentService.createComment(userDetails.getUser(), postId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(
            CommonResponse.<Void>builder().message("댓글 생성 완료").build());
    }

    @Operation(summary = "댓글 수정", description = "댓글을 수정할 수 있는 API")
    @PutMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommonResponse<Void>> updateComment(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable Long postId,
        @PathVariable Long commentId,
        @RequestBody CommentRequestDto requestDto) {
        commentService.updateComment(userDetails.getUser(), postId, commentId, requestDto);
        return ResponseEntity.status(HttpStatus.OK.value()).body(
            CommonResponse.<Void>builder().message("댓글 수정 완료").build());
    }

    @Operation(summary = "댓글 삭제", description = "댓글을 삭제할 수 있는 API")
    @DeleteMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommonResponse<Void>> deleteComment(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable Long postId,
        @PathVariable Long commentId) {
        commentService.deleteComment(userDetails.getUser(), postId, commentId);
        return ResponseEntity.status(HttpStatus.OK.value()).body(
            CommonResponse.<Void>builder().message("댓글 삭제 완료").build());
    }
}

