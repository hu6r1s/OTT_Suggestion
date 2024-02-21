package com.three.ott_suggestion.post;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.three.ott_suggestion.auth.filter.MockSpringSecurityFilter;
import com.three.ott_suggestion.global.config.SecurityConfig;
import com.three.ott_suggestion.global.util.UserDetailsImpl;
import com.three.ott_suggestion.post.controller.PostController;
import com.three.ott_suggestion.post.dto.PostRequestDto;
import com.three.ott_suggestion.post.dto.PostResponseDto;
import com.three.ott_suggestion.post.entity.Post;
import com.three.ott_suggestion.post.service.PostService;
import com.three.ott_suggestion.user.controller.AuthController;
import com.three.ott_suggestion.user.entity.User;
import com.three.ott_suggestion.user.service.AuthService;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest(
    controllers = {AuthController.class, PostController.class},
    excludeFilters = {
        @ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = SecurityConfig.class
        )
    }
)
public class PostControllerTest {

    private MockMvc mvc;
    private Principal mockPrincipal;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    AuthService authService;

    @MockBean
    PostService postService;

    private List<PostResponseDto> postList = new ArrayList<>();

    private User user;
    private Post post;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
            .apply(springSecurity(new MockSpringSecurityFilter()))
            .build();
    }

    @BeforeEach
    void mockUserSetup() {
        String email = "test@test.com";
        String password = "ASDasd123!!!";
        String nickname = "test";
        String introduction = "test";
        User testUser = new User(email, password, nickname, introduction, null, true);
        UserDetailsImpl testUserDetails = new UserDetailsImpl(testUser);
        mockPrincipal = new UsernamePasswordAuthenticationToken(testUserDetails, null, null);
        user = new User(email, password, nickname, introduction, null, true);
    }

    void mockPostSetup() {
        Post post1 = new Post(new PostRequestDto("title1", "content1"), user);
        Post post2 = new Post(new PostRequestDto("title2", "content2"), user);
        postList.add(new PostResponseDto(post1, ""));
        postList.add(new PostResponseDto(post2, ""));
        post = new Post(new PostRequestDto("test title", "test content"), user);
    }

    @Test
    @DisplayName("게시글 작성")
    void 게시글_작성() throws Exception {
        // given
        String title = "test title";
        String content = "test content";
        PostRequestDto requestDto = new PostRequestDto(title, content);

        // when - then
        mvc.perform(multipart("/posts")
            .file(new MockMultipartFile(
                "dto",
                "",
                "application/json",
                objectMapper.writeValueAsString(requestDto).getBytes(StandardCharsets.UTF_8))
            )
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .accept(MediaType.APPLICATION_JSON)
            .principal(mockPrincipal)
        )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.message").value("게시글 생성이 완료되었습니다."))
            .andDo(print());
    }

    @Test
    @DisplayName("게시물 전체 조회")
    void 게시글_전체_조회() throws Exception {
        // given
        mockPostSetup();
        when(postService.getAllPosts()).thenReturn(postList);

        // when - then
        mvc.perform(get("/posts"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data").exists())
            .andDo(print());
    }

    @Test
    @DisplayName("게시물 상세 조회")
    void 게시글_상세_조회() throws Exception {
        // given
        mockPostSetup();
        when(postService.getPost(post.getId())).thenReturn(new PostResponseDto(post));

        // when - then
        mvc.perform(get("/posts/{postId}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print());
    }

    @Test
    @DisplayName("게시글 수정")
    void 게시글_수정() throws Exception {
        // given
        mockPostSetup();
        PostRequestDto request = new PostRequestDto("update title", "update content");
        doNothing().when(postService).updatePost(user.getId(), post.getId(), request, null);

        // when - then
        MockMultipartHttpServletRequestBuilder builder = multipart("/posts/{postId}", 1);
        builder.with(new RequestPostProcessor() {
            @Override
            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                request.setMethod("PUT");
                return request;
            }
        });

        mvc.perform(builder.file(new MockMultipartFile(
                    "dto",
                    "",
                    "application/json",
                    objectMapper.writeValueAsString(request).getBytes(StandardCharsets.UTF_8)))
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept(MediaType.APPLICATION_JSON)
                .principal(mockPrincipal))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("게시글이 수정되었습니다."))
            .andDo(print());
    }

    @Test
    @DisplayName("게시물 삭제")
    void 게시글_삭제() throws Exception {
        // given
        mockPostSetup();
        PostRequestDto request = new PostRequestDto("update title", "update content");
        doNothing().when(postService).deletePost(user, post.getId());

        // when - then
        mvc.perform(delete("/posts/{postId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .principal(mockPrincipal))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("게시글 삭제가 완료되었습니다"))
            .andDo(print());
    }
}
