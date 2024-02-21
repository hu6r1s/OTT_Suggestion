package com.three.ott_suggestion.post;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.three.ott_suggestion.global.exception.InvalidInputException;
import com.three.ott_suggestion.global.exception.InvalidPostException;
import com.three.ott_suggestion.image.service.PostImageService;
import com.three.ott_suggestion.post.dto.PostRequestDto;
import com.three.ott_suggestion.post.dto.PostResponseDto;
import com.three.ott_suggestion.post.entity.Post;
import com.three.ott_suggestion.post.repository.PostRepository;
import com.three.ott_suggestion.post.service.PostService;
import com.three.ott_suggestion.user.entity.User;
import com.three.ott_suggestion.user.service.UserService;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @InjectMocks
    private PostService postService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserService userService;

    @Mock
    private PostImageService postImageService;

    private User user;
    private Post post;
    private List<PostResponseDto> mockPostList = new ArrayList<>();

    @BeforeEach
    void setup() {
        String email = "test@test.com";
        String password = "ASDasd123!!!";
        String nickname = "test";
        String introduction = "test";
        user = new User(email, password, nickname, introduction, null, true);
        post = new Post(new PostRequestDto("test title", "test content"), user);
    }

    void mockPostSetup() {
        Post post1 = new Post(new PostRequestDto("title1", "content1"), user);
        Post post2 = new Post(new PostRequestDto("title1", "content1"), user);
        mockPostList.add(new PostResponseDto(post1, ""));
        mockPostList.add(new PostResponseDto(post2, ""));
    }

    @Test
    @DisplayName("게시글 작성")
    void 게시글_작성() throws Exception {
        // given
        String title = "test title";
        String content = "test content";

        PostRequestDto requestDto = new PostRequestDto(title, content);

        // when
        postService.createPost(requestDto, user, null);

        // then
        assertDoesNotThrow(() -> true);
    }

    @Test
    @DisplayName("게시글 전체 조회")
    void 게시글_전체_조회() {
        //given
        mockPostSetup();
        given(postRepository.findAllByDeletedAtIsNullOrderByCreatedAtDesc())
            .willReturn(mockPostList.stream().map(postResponseDto -> {
                return new Post(
                    new PostRequestDto(postResponseDto.getTitle(), postResponseDto.getContent()
                    ), user);
            }).collect(Collectors.toList()));

        // when
        List<PostResponseDto> response = postService.getAllPosts();

        // then
        assertEquals(mockPostList.size(), response.size());
    }

    @Test
    @DisplayName("게시글 상세 조회")
    void 게시글_상세_조회() throws MalformedURLException {
        // given
        mockPostSetup();
        given(postRepository.findPostByIdAndDeletedAtIsNull(mockPostList.get(0).getId()))
            .willReturn(Optional.of(new Post(
                new PostRequestDto(
                    mockPostList.get(0).getTitle(), mockPostList.get(0).getContent()
                ),
            user)));

        // when
        PostResponseDto response = postService.getPost(mockPostList.get(0).getId());

        // then
        assertDoesNotThrow(() -> true);
        assertEquals(mockPostList.get(0).getId(), response.getId());
    }

    @Test
    @DisplayName("게시글 상세 조회 - 에러 처리")
    void 게시글_조회_에러() throws MalformedURLException {
        mockPostSetup();
        given(postRepository.findPostByIdAndDeletedAtIsNull(mockPostList.get(0).getId()))
            .willReturn(Optional.empty());

        // when - then
        InvalidInputException e = assertThrows(InvalidInputException.class, () -> {
            postService.getPost(mockPostList.get(0).getId());
        });
        assertEquals("해당 게시글은 삭제 되었습니다.", e.getMessage());
    }

    @Test
    @DisplayName("게시글 수정")
    void 게시글_수정() throws IOException {
        // given
        mockPostSetup();

        given(postRepository.findById(mockPostList.get(0).getId()))
            .willReturn(Optional.of(new Post(
                new PostRequestDto(
                    mockPostList.get(0).getTitle(), mockPostList.get(0).getContent()
            ),
                user)));

        PostRequestDto request = new PostRequestDto("update title", "update content");

        // when
        postService.updatePost(
            user.getId(),
            mockPostList.get(0).getId(),
            request,
            null
        );

        // then
        Optional<Post> updatePost = postRepository.findById(mockPostList.get(0).getId());
        assertEquals(request.getTitle(), updatePost.get().getTitle());
        assertEquals(request.getContent(), updatePost.get().getContent());
    }

    @Test
    @DisplayName("게시글 수정 에러")
    void 게시글_수정_에러() {
        // given
        mockPostSetup();

        given(postRepository.findById(mockPostList.get(0).getId()))
            .willReturn(Optional.empty());

        PostRequestDto request = new PostRequestDto("update title", "update content");

        // when - then
        InvalidPostException e = assertThrows(InvalidPostException.class, () -> {
            postService.updatePost(
                user.getId(),
                mockPostList.get(0).getId(),
                request,
                null
            );
        });
        assertEquals("해당 게시글이 없습니다.", e.getMessage());
    }

    @Test
    @DisplayName("게시글 삭제")
    void 게시글_삭제() {
        // given
        mockPostSetup();

        given(postRepository.findById(mockPostList.get(0).getId()))
            .willReturn(Optional.of(new Post(
                new PostRequestDto(
                    mockPostList.get(0).getTitle(), mockPostList.get(0).getContent()
                ),
                user)));

        // when
        postService.deletePost(
            user,
            mockPostList.get(0).getId()
        );

        // then
        Optional<Post> updatePost = postRepository.findById(mockPostList.get(0).getId());
        assertNotNull(updatePost.get().getDeletedAt());
    }

    @Test
    @DisplayName("게시글 키워드 조회 - 제목으로")
    void 게시글_키워드_제목으로_조회() {
        // Given
        mockPostSetup();
        given(postRepository.findByTitleContains("1"))
            .willReturn(mockPostList.stream().map(postResponseDto -> {
                return new Post(
                    new PostRequestDto(postResponseDto.getTitle(), postResponseDto.getContent()
                    ), user);
            }).collect(Collectors.toList()));

        // When
        List<List<PostResponseDto>> response = postService.searchPost("title", "1");

        // Then
        assertEquals(mockPostList.get(0).getTitle(), response.get(0).get(0).getTitle());
    }

    @Test
    @DisplayName("게시글 키워드 조회 - 작성자로")
    void 게시글_키워드_작성자로_조회() {
        // Given
        mockPostSetup();
        given(userService.findContainUser(anyString())).willReturn(List.of(user));
        given(postRepository.findByUserId(user.getId()))
            .willReturn(mockPostList.stream().map(postResponseDto -> {
                return new Post(
                    new PostRequestDto(postResponseDto.getTitle(), postResponseDto.getContent()
                    ), user);
            }).collect(Collectors.toList()));

        // When
        List<List<PostResponseDto>> response = postService.searchPost("nickname", "test");

        // Then
        assertEquals(mockPostList.get(0).getTitle(), response.get(0).get(0).getTitle());
    }
}
