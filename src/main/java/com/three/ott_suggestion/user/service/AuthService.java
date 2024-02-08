package com.three.ott_suggestion.user.service;

import com.three.ott_suggestion.user.dto.SignupRequestDto;
import com.three.ott_suggestion.user.entity.RefreshToken;
import com.three.ott_suggestion.user.entity.User;
import com.three.ott_suggestion.user.repository.RefreshTokenRepository;
import com.three.ott_suggestion.user.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;

    public void signup(SignupRequestDto signupRequestDto) {

        String email = signupRequestDto.getEmail();
        String password = passwordEncoder.encode(signupRequestDto.getPassword());
        String nickname = signupRequestDto.getNickname();
        String introduction = signupRequestDto.getIntroduction();

        // 회원 중복 확인
        Optional<User> checkUserEmail = userRepository.findByEmail(email);
        if (checkUserEmail.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "중복된 email 입니다.");
        }

        User user = new User(email, password, nickname, introduction);
        userRepository.save(user);
    }

    public void logout(UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(() ->
            new IllegalArgumentException("User가 존재하지 않습니다.")
        );
        RefreshToken refreshToken = refreshTokenRepository.findByUserId(user.getId());
        refreshTokenRepository.delete(refreshToken);
    }
}
