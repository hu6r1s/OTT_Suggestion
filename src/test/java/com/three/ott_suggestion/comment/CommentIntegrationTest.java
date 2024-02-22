package com.three.ott_suggestion.comment;

import static org.junit.jupiter.api.Assertions.*;

import com.three.ott_suggestion.comment.dto.CommentRequestDto;
import com.three.ott_suggestion.comment.dto.CommentResponseDto;
import com.three.ott_suggestion.post.dto.PostRequestDto;
import com.three.ott_suggestion.user.dto.LoginRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CommentIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String authToken;

    @BeforeEach
    public void setup() {
        String loginUrl = "http://localhost:" + port + "/auth/login";
        LoginRequestDto loginRequest = new LoginRequestDto("testUser", "testPassword");
        ResponseEntity<Void> loginResponse = restTemplate
            .postForEntity(loginUrl, loginRequest, Void.class);

        HttpHeaders headers = loginResponse.getHeaders();
        authToken = headers.getFirst(HttpHeaders.AUTHORIZATION);
    }

    @Test
    @DisplayName("댓글 생성")
    public void testCreateComment() {
        // given
        Long postId = 1L;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        HttpEntity<CommentRequestDto> requestEntity = new HttpEntity<>(
            new CommentRequestDto("새로운 댓글 내용"), headers
        );

        // when
        ResponseEntity<CommentResponseDto> responseEntity = restTemplate.postForEntity(
            "/posts/{postId}/comments", requestEntity, CommentResponseDto.class, postId);

        // then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("댓글 수정")
    public void testUpdateComment() {
        // given
        Long postId = 1L;
        Long commentId = 1L;
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        HttpEntity<CommentRequestDto> requestEntity = new HttpEntity<>(
            new CommentRequestDto("새로운 댓글 내용"), headers
        );

        // when
        restTemplate.put("/posts/{postId}/comments/{commentId}", requestEntity, postId, commentId);

        // then
        ResponseEntity<CommentResponseDto> getResponseEntity = restTemplate.getForEntity(
            "/posts/{postId}/comments/{commentId}", CommentResponseDto.class, postId, commentId);

        assertEquals(HttpStatus.OK, getResponseEntity.getStatusCode());
        assertNotNull(getResponseEntity.getBody());
        assertEquals("수정된 댓글 내용", getResponseEntity.getBody().getContent());
    }

    @Test
    @DisplayName("댓글 삭제")
    public void testDeleteComment() {
        Long postId = 1L; // 댓글이 달린 게시글 ID
        Long commentId = 1L; // 삭제할 댓글 ID

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        restTemplate.delete("/posts/{postId}/comments/{commentId}", postId, commentId);

        // 삭제된 댓글 조회
        ResponseEntity<CommentResponseDto> getResponseEntity = restTemplate.getForEntity(
            "/posts/{postId}/comments/{commentId}", CommentResponseDto.class, postId, commentId);

        assertEquals(HttpStatus.OK, getResponseEntity.getStatusCode());
        assertNull(getResponseEntity.getBody());
    }

}
