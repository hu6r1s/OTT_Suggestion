package com.three.ott_suggestion.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.three.ott_suggestion.user.dto.SignupRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("회원가입 성공 테스트")
    public void testSignupSuccess() {
        // given
        String email = "asd@asd.com";
        String password = "asdASD123!!!";
        String nickname = "test nick";
        String intruduction = "test";

        // when
        String signupUrl = "http://localhost:" + port + "/auth/signup";
        SignupRequestDto requestDto = new SignupRequestDto(email, password, nickname, intruduction);
        ResponseEntity<Void> responseEntity = restTemplate
            .postForEntity(signupUrl, requestDto, Void.class);

        // then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("회원가입 실패 테스트")
    public void testSignupFailed() {
        // given
        String email = "asd@asd.com";
        String password = "asdASD123!!!";
        String nickname = "test nick";
        String intruduction = "test";

        // when
        String signupUrl = "http://localhost:" + port + "/auth/signup";
        SignupRequestDto requestDto = new SignupRequestDto(email, password, nickname, intruduction);
        ResponseEntity<Void> responseEntity = restTemplate
            .postForEntity(signupUrl, requestDto, Void.class);

        // then
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }
}
