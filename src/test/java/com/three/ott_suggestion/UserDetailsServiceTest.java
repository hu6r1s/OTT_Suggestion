package com.three.ott_suggestion;

import com.three.ott_suggestion.global.util.UserDetailsImpl;
import com.three.ott_suggestion.global.util.UserDetailsServiceImpl;
import com.three.ott_suggestion.post.dto.PostRequestDto;
import com.three.ott_suggestion.post.entity.Post;
import com.three.ott_suggestion.user.entity.User;
import com.three.ott_suggestion.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    private User user;

    @BeforeEach
    void setup() {
        String email = "test@test.com";
        String password = "ASDasd123!!!";
        String nickname = "test";
        String introduction = "test";
        user = new User(email, password, nickname, introduction, null, true);
    }

    @Test
    @DisplayName("사용자 정보 로드 - 성공")
    void loadUserByUsername_Success() {
        // given
        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));

        // when
        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService
            .loadUserByUsername(user.getEmail());

        // then
        assertEquals(user.getEmail(), userDetails.getUsername());
    }

    @Test
    @DisplayName("사용자 정보 로드 - 실패 (사용자가 없는 경우)")
    void loadUserByUsername_UserNotFound() {
        // given
        String email = "notfound@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // when - then
        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername(email);
        });
    }
}
