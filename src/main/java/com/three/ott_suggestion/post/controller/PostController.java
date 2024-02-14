package com.three.ott_suggestion.post.controller;

import com.three.ott_suggestion.global.response.CommonResponse;
import com.three.ott_suggestion.global.util.UserDetailsImpl;
import com.three.ott_suggestion.post.dto.PostRequestDto;
import com.three.ott_suggestion.post.dto.PostResponseDto;
import com.three.ott_suggestion.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Tag(name = "Post", description = "게시글 컨트롤러")
public class PostController {

    private final PostService postService;

    @Operation(summary = "게시글 생성", description = "게시글을 작성할 수 있는 API")
    @PostMapping("/posts")
    public ResponseEntity<CommonResponse<Void>> createPost(
        @RequestPart PostRequestDto requestDto,
        @RequestPart MultipartFile image,
        @AuthenticationPrincipal
        UserDetailsImpl userDetails) throws Exception {
        postService.createPost(requestDto, userDetails.getUser(), image);
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(
            CommonResponse.<Void>builder().message("게시물 생성 완료").build()
        );
    }

    @Operation(summary = "게시글 전체 조회", description = "게시글 전체 조회할 수 있는 API")
    @GetMapping("/posts")
    public ResponseEntity<CommonResponse<List<PostResponseDto>>> getAllPosts() {
        List<PostResponseDto> postResponseDtos = postService.getAllPosts();
        return ResponseEntity.ok().body(CommonResponse.<List<PostResponseDto>>builder()
            .data(postResponseDtos).build());
    }

    @Operation(summary = "게시글 상세 조회", description = "게시글 상세 조회할 수 있는 API")
    @GetMapping("/posts/{postId}")
    public ResponseEntity<CommonResponse<PostResponseDto>> getPost(@PathVariable Long postId)
        throws MalformedURLException {
        PostResponseDto postResponseDto = postService.getPost(postId);
        return ResponseEntity.ok().body(CommonResponse.<PostResponseDto>builder()
            .data(postResponseDto).build());
    }

    @Operation(summary = "게시글 특정 조회", description = "게시글을 특정 키워드로 조회할 수 있는 API")
    @GetMapping("/posts/search")
    public ResponseEntity<CommonResponse<List<List<PostResponseDto>>>> searchPost(
        @RequestParam String type,
        @RequestParam String keyword
    ) {
        return ResponseEntity.ok().body(CommonResponse.<List<List<PostResponseDto>>>builder()
            .data(postService.searchPost(type, keyword)).build());
    }

    @Operation(summary = "게시글 수정", description = "게시글을 수정할 수 있는 API")
    @PutMapping("/posts/{postId}")
    public ResponseEntity<CommonResponse<PostResponseDto>> updatePost(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable Long postId,
        @RequestPart PostRequestDto requestDto,
        @RequestPart(required = false) MultipartFile image
    ) throws IOException {
        PostResponseDto responseDto = postService.updatePost(userDetails.getUser().getId(), postId,
            requestDto, image);
        return ResponseEntity.status(HttpStatus.OK.value()).body(
            CommonResponse.<PostResponseDto>builder().message("특정 게시물 수정 완료").data(responseDto)
                .build()
        );
    }

    @Operation(summary = "게시글 삭제", description = "게시글 삭제할 수 있는 API")
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<CommonResponse<Void>> deletePost(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable Long postId) {
        postService.deletePost(userDetails.getUser(), postId);
        return ResponseEntity.status(HttpStatus.OK.value()).body(
            CommonResponse.<Void>builder().message("게시물 삭제 완료").build());
    }
}
