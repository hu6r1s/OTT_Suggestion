package com.three.ott_suggestion.post.controller;

import com.three.ott_suggestion.global.response.CommonResponse;
import com.three.ott_suggestion.post.dto.PostRequestDto;
import com.three.ott_suggestion.post.service.PostService;
import com.three.ott_suggestion.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/posts")
    public ResponseEntity<CommonResponse<Void>> createPost(@RequestBody PostRequestDto requestDto,
            @AuthenticationPrincipal
            UserDetails userDetails, User user) {
        postService.createPost(requestDto , user);
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(
                CommonResponse.<Void>builder().message("일정 생성 완료").build()
        );
    }
}
