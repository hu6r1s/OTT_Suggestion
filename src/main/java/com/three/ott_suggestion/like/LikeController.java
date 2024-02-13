package com.three.ott_suggestion.like;

import com.three.ott_suggestion.global.response.CommonResponse;
import com.three.ott_suggestion.global.util.UserDetailsImpl;
import com.three.ott_suggestion.post.dto.PostResponseDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @GetMapping("/posts/{postId}/like")
    public ResponseEntity<CommonResponse<Long>> countLikes(@PathVariable Long postId) {
        return ResponseEntity.ok().body(CommonResponse.<Long>builder()
            .data(likeService.countLikes(postId)).build());
    }

    @PostMapping("/posts/{postId}/like")
    public ResponseEntity<CommonResponse<Void>> likePost(@PathVariable Long postId,
        @AuthenticationPrincipal
        UserDetailsImpl userDetails) {
        return ResponseEntity.ok().body(CommonResponse.<Void>builder()
            .message(likeService.likePost(postId, userDetails.getUser())).build());

    }

    @GetMapping("/posts/like/top3")
    public ResponseEntity<CommonResponse<List<PostResponseDto>>> getLikeTopThreePosts() {
        return ResponseEntity.ok().body(CommonResponse.<List<PostResponseDto>>builder()
            .data(likeService.getLikeTopThreePosts())
            .build());
    }
}
