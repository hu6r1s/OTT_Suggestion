package com.three.ott_suggestion.like.controller;

import com.three.ott_suggestion.global.response.CommonResponse;
import com.three.ott_suggestion.global.util.UserDetailsImpl;
import com.three.ott_suggestion.like.service.LikeService;
import com.three.ott_suggestion.post.dto.PostResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.MalformedURLException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@Tag(name = "Like", description = "좋아요 컨트롤러")
public class LikeController {

    private final LikeService likeService;

    @Operation(summary = "좋아요 개수", description = "좋아요 개수 조회할 수 있는 API")
    @GetMapping("/posts/{postId}/like")
    public ResponseEntity<CommonResponse<Long>> countLikes(@PathVariable Long postId) {
        return ResponseEntity.ok().body(CommonResponse.<Long>builder()
            .data(likeService.countLikes(postId)).build());
    }

    @Operation(summary = "좋아요 선택", description = "좋아요 선택할 수 있는 API")
    @PostMapping("/posts/{postId}/like")
    public ResponseEntity<CommonResponse<Void>> createLike(@PathVariable Long postId,
        @AuthenticationPrincipal
        UserDetailsImpl userDetails) {
        return ResponseEntity.ok().body(CommonResponse.<Void>builder()
            .message(likeService.createLike(postId, userDetails.getUser())).build());
    }

    @Operation(summary = "좋아요 취소", description = "좋아요 취소할 수 있는 API")
    @DeleteMapping("/posts/{postId}/like")
    public ResponseEntity<CommonResponse<Void>> deleteLike(@PathVariable Long postId,
        @AuthenticationPrincipal
        UserDetailsImpl userDetails) {
        return ResponseEntity.ok().body(CommonResponse.<Void>builder()
            .message(likeService.deleteLike(postId, userDetails.getUser())).build());
    }

    @Operation(summary = "좋아요 랭킹 조회", description = "좋아요가 높은 3개의 게시글 조회할 수 있는 API")
    @GetMapping("/posts/like/top3")
    public ResponseEntity<CommonResponse<List<PostResponseDto>>> getLikeTopThreePosts()
        throws MalformedURLException {
        return ResponseEntity.ok().body(CommonResponse.<List<PostResponseDto>>builder()
            .data(likeService.getLikeTopThreePosts())
            .build());
    }
}
