package com.three.ott_suggestion.auth;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

import com.three.ott_suggestion.image.repository.UserImageRepository;
import com.three.ott_suggestion.user.dto.SignupRequestDto;
import com.three.ott_suggestion.user.entity.User;
import com.three.ott_suggestion.user.repository.RefreshTokenRepository;
import com.three.ott_suggestion.user.repository.UserRepository;
import com.three.ott_suggestion.user.service.AuthService;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    private HttpServletRequest servletRequest = new MockHttpServletRequest();

    @InjectMocks
    private AuthService authService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserImageRepository userImageRepository;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("회원가입 성공")
    void 회원가입_성공() throws MessagingException, UnsupportedEncodingException {
        // given
        String email = "darmaa333@gmail.com";
        String password = "ASDasd123!!!";
        String nickname = "test";
        String introduction = "test";
        String siteUrl = servletRequest.getRequestURL().toString().replace(
            servletRequest.getServletPath(), ""
        );

        SignupRequestDto requestDto = new SignupRequestDto(email, password, nickname, introduction);

        given(userRepository.findByEmail(email)).willReturn(Optional.empty());
        given(mailSender.createMimeMessage()).willReturn(new MimeMessage((Session) null));

        // when
        authService.signup(requestDto, siteUrl);

        // then
        assertDoesNotThrow(() -> authService.signup(requestDto, siteUrl));
    }

    @Test
    @DisplayName("이메일 중복")
    void 이메일_중복() throws MessagingException, UnsupportedEncodingException {
        // given
        String email = "darmaa333@gmail.com";
        String password = "ASDasd123";
        String nickname = "test";
        String introduction = "test";
        String siteUrl = servletRequest.getRequestURL().toString().replace(
            servletRequest.getServletPath(), ""
        );

        SignupRequestDto requestDto = new SignupRequestDto(email, password, nickname, introduction);

        given(userRepository.findByEmail(email)).willReturn(Optional.of(new User()));

        // when - then
        ResponseStatusException e = assertThrows(ResponseStatusException.class, () -> {
            authService.signup(requestDto, siteUrl);
        });
        assertEquals("400 BAD_REQUEST \"중복된 email 입니다.\"", e.getMessage());
    }
}
