package com.three.ott_suggestion.comment;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.three.ott_suggestion.auth.filter.MockSpringSecurityFilter;
import com.three.ott_suggestion.comment.controller.CommentController;
import com.three.ott_suggestion.comment.dto.CommentRequestDto;
import com.three.ott_suggestion.comment.dto.CommentResponseDto;
import com.three.ott_suggestion.comment.entity.Comment;
import com.three.ott_suggestion.comment.service.CommentService;
import com.three.ott_suggestion.global.config.SecurityConfig;
import com.three.ott_suggestion.global.util.UserDetailsImpl;
import com.three.ott_suggestion.post.dto.PostRequestDto;
import com.three.ott_suggestion.post.entity.Post;
import com.three.ott_suggestion.user.controller.AuthController;
import com.three.ott_suggestion.user.entity.User;
import com.three.ott_suggestion.user.service.AuthService;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
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
    controllers = {AuthController.class, CommentController.class},
    excludeFilters = {
        @ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = SecurityConfig.class
        )
    }
)
public class CommentControllerTest {
    private MockMvc mvc;
    private Principal mockPrincipal;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    AuthService authService;

    @MockBean
    CommentService commentService;

    private User user;

    private Post post;

    private List<CommentResponseDto> commentList = new ArrayList<>();

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
        post = new Post(new PostRequestDto("test titme", "test content"), user);
    }

    void mockCommentSetup() {
        Comment comment1 = new Comment(new CommentRequestDto("comment1"), post, user);
        Comment comment2 = new Comment(new CommentRequestDto("comment2"), post, user);
        commentList.add(new CommentResponseDto(comment1));
        commentList.add(new CommentResponseDto(comment2));
    }

    @Test
    @DisplayName("댓글 생성")
    void 댓글_생성() throws Exception {
        // given
        CommentRequestDto requestDto = new CommentRequestDto("test content");

        // when - then
        mvc.perform(post("/posts/{postId}/comments", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto))
                .principal(mockPrincipal))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.message").value("댓글 생성 완료"))
            .andDo(print());
    }

    @Test
    @DisplayName("댓글 조회")
    void 댓글_조회() throws Exception {
        // given
        mockCommentSetup();
        when(commentService.getComments(post.getId())).thenReturn(commentList);

        // when - then
        mvc.perform(get("/posts/{postId}/comments", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .principal(mockPrincipal))
            .andExpect(status().isOk())
            .andDo(print());
    }

    @Test
    @DisplayName("댓글 수정")
    void 댓글_수정() throws Exception {
        // given
        mockCommentSetup();
        CommentRequestDto request = new CommentRequestDto("update content");
        doNothing().when(commentService)
            .updateComment(user, post.getId(), commentList.get(0).getId(), request);

        // when - then
        mvc.perform(put("/posts/{postId}/comments/{commentId}", 1L, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request).getBytes(StandardCharsets.UTF_8))
                .accept(MediaType.APPLICATION_JSON)
                .principal(mockPrincipal))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("댓글 수정 완료"))
            .andDo(print());
    }

    @Test
    @DisplayName("댓글 삭제")
    void 댓글_삭제() throws Exception {
        // given
        mockCommentSetup();
        doNothing().when(commentService)
            .deleteComment(user, post.getId(), commentList.get(0).getId());

        // when - when
        mvc.perform(delete("/posts/{postId}/comments/{commentId}", 1L, 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .principal(mockPrincipal))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("댓글 삭제 완료"))
            .andDo(print());
    }
}
