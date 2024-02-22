package com.three.ott_suggestion.post;

import com.three.ott_suggestion.post.dto.PostRequestDto;
import com.three.ott_suggestion.post.dto.PostResponseDto;
import com.three.ott_suggestion.user.dto.LoginRequestDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostIntegrationTest {

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
    @DisplayName("게시글 생성")
    public void testCreatePost() {
        // given
        String title = "Test Title";
        String content = "Test Content";

        String createUrl = "http://localhost:" + port + "/posts";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        HttpEntity<PostRequestDto> requestEntity = new HttpEntity<>(
            new PostRequestDto(title, content), headers
        );

        // when
        ResponseEntity<Void> responseEntity = restTemplate
            .postForEntity(createUrl, requestEntity, Void.class);

        // then
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("게시글 조회")
    public void testGetPost() {
        // given
        Long postId = 1L;
        String getUrl = "http://localhost:" + port + "/posts/" + postId;

        // when
        ResponseEntity<PostResponseDto> responseEntity = restTemplate
            .getForEntity(getUrl, PostResponseDto.class);

        // then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    @DisplayName("게시글 수정")
    public void testUpdatePost() {
        // given
        Long postId = 1L;
        String updatedTitle = "Updated Title";
        String updatedContent = "Updated Content";

        String updateUrl = "http://localhost:" + port + "/posts/" + postId;
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);

        // when
        HttpEntity<PostRequestDto> requestEntity = new HttpEntity<>(
            new PostRequestDto(updatedTitle, updatedContent), headers
        );
        restTemplate.put(updateUrl, requestEntity);

        // then
        String getUrl = "http://localhost:" + port + "/posts/" + postId;
        ResponseEntity<PostResponseDto> responseEntity = restTemplate
            .getForEntity(getUrl, PostResponseDto.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(updatedTitle, responseEntity.getBody().getTitle());
        assertEquals(updatedContent, responseEntity.getBody().getContent());
    }

    @Test
    @DisplayName("게시글 삭제")
    public void testDeletePost() {
        // given
        Long postId = 1L;
        String deleteUrl = "http://localhost:" + port + "/posts/" + postId;
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);

        // when
        restTemplate.delete(deleteUrl);
        ResponseEntity<Void> responseEntity = restTemplate.getForEntity(deleteUrl, Void.class);

        // then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
}
