package com.three.ott_suggestion.post.controller;

import com.three.ott_suggestion.global.response.CommonResponse;
import com.three.ott_suggestion.post.dto.PostRequestDto;
import com.three.ott_suggestion.post.dto.PostResponseDto;
import com.three.ott_suggestion.post.service.PostService;
import com.three.ott_suggestion.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
        postService.createPost(requestDto, user);
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(
            CommonResponse.<Void>builder().message("일정 생성 완료").build()
        );
    }

    @GetMapping("/posts")
    public ResponseEntity<CommonResponse<List<PostResponseDto>>> getAllPosts() {
        List<PostResponseDto> postResponseDtos = postService.getAllPosts();
        return ResponseEntity.ok().body(CommonResponse.<List<PostResponseDto>>builder()
            .data(postResponseDtos).build());

    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<CommonResponse<PostResponseDto>> getPost(@PathVariable Long postId) {
        PostResponseDto postResponseDto = postService.getPost(postId);
        return ResponseEntity.ok().body(CommonResponse.<PostResponseDto>builder()
            .data(postResponseDto).build());
    }

    @PutMapping("/posts/{postId}")
    public ResponseEntity<CommonResponse<PostResponseDto>> updatePost(
            @PathVariable Long postId,
            @RequestBody PostRequestDto requestDto
    ) {
        PostResponseDto responseDto = postService.updatePost(postId, requestDto);
        return ResponseEntity.status(HttpStatus.OK.value()).body(
                CommonResponse.<PostResponseDto>builder().message("특정 게시물 조회 완료").data(responseDto)
                        .build()
        );
    }
}
