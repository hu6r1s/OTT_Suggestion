package com.three.ott_suggestion.comment.controller;

import com.three.ott_suggestion.comment.dto.CommentRequestDto;
import com.three.ott_suggestion.comment.service.CommentService;
import com.three.ott_suggestion.global.response.CommonResponse;
import com.three.ott_suggestion.global.util.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommonResponse<Void>> createComment(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable Long postId,
        @RequestBody CommentRequestDto requestDto) {
        commentService.createComment(userDetails.getUser(), postId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(
            CommonResponse.<Void>builder().message("댓글 생성 완료").build());
    }

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

