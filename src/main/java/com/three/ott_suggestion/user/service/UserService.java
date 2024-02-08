package com.three.ott_suggestion.user.service;

import com.three.ott_suggestion.global.util.UserDetailsImpl;
import com.three.ott_suggestion.user.dto.UpdateRequestDto;
import com.three.ott_suggestion.user.dto.UserResponseDto;
import com.three.ott_suggestion.user.entity.User;
import com.three.ott_suggestion.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponseDto getUserInfo(UserDetailsImpl userDetails) {
        return new UserResponseDto(userDetails.getUser());
    }

    @Transactional
    public void updateUserInfo(UserDetailsImpl userDetails, UpdateRequestDto requestDto) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(() ->
            new IllegalArgumentException("User가 존재하지 않습니다.")
        );
        user.update(requestDto);
    }
}
