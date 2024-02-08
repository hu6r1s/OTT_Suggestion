package com.three.ott_suggestion.comment.controller;

import com.three.ott_suggestion.comment.dto.CommentRequestDto;
import com.three.ott_suggestion.comment.dto.CommentResponseDto;
import com.three.ott_suggestion.comment.service.CommentService;
import com.three.ott_suggestion.global.response.CommonResponse;
import com.three.ott_suggestion.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommonResponse<Void>> createComment(
        @AuthenticationPrincipal UserDetails userDetails,
        @PathVariable Long postId,
        @RequestBody CommentRequestDto requestDto,
        User user) {
        commentService.createComment(user, postId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(
            CommonResponse.<Void>builder().message("댓글 생성 완료").build());
    }
}

