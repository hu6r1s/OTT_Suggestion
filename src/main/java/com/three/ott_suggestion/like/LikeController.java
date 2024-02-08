package com.three.ott_suggestion.like;

import com.three.ott_suggestion.global.response.CommonResponse;
import com.three.ott_suggestion.global.util.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/posts/{postId}/like")
    public ResponseEntity<CommonResponse<Void>> likePost(@PathVariable Long postId,
        @AuthenticationPrincipal
        UserDetailsImpl userDetails) {
        return ResponseEntity.ok().body(CommonResponse.<Void>builder()
            .message(likeService.likePost(postId, userDetails.getUser())).build());

    }

}
