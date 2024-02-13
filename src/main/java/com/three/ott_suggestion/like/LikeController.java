package com.three.ott_suggestion.like;

import com.three.ott_suggestion.global.response.CommonResponse;
import com.three.ott_suggestion.global.util.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@Tag(name = "Like", description = "좋아요 컨트롤러")
public class LikeController {

    private final LikeService likeService;

    @Operation(summary = "좋아요", description = "좋아요 할 수 있는 API")
    @GetMapping("/posts/{postId}/like")
    public ResponseEntity<CommonResponse<Long>> countLikes(@PathVariable Long postId) {
        return ResponseEntity.ok().body(CommonResponse.<Long>builder()
            .data(likeService.countLikes(postId)).build());
    }

    @Operation(summary = "좋아요 취소", description = "좋아요 취소할 수 있는 API")
    @PostMapping("/posts/{postId}/like")
    public ResponseEntity<CommonResponse<Void>> likePost(@PathVariable Long postId,
        @AuthenticationPrincipal
        UserDetailsImpl userDetails) {
        return ResponseEntity.ok().body(CommonResponse.<Void>builder()
            .message(likeService.likePost(postId, userDetails.getUser())).build());

    }

}
