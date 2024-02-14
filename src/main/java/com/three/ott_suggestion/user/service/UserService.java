package com.three.ott_suggestion.user.service;

import com.three.ott_suggestion.global.exception.InvalidInputException;
import com.three.ott_suggestion.global.util.UserDetailsImpl;
import com.three.ott_suggestion.image.service.UserImageService;
import com.three.ott_suggestion.user.dto.UpdateRequestDto;
import com.three.ott_suggestion.user.dto.UserResponseDto;
import com.three.ott_suggestion.user.entity.User;
import com.three.ott_suggestion.user.repository.UserRepository;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserImageService userImageService;

    public UserResponseDto getUserInfo(UserDetailsImpl userDetails) throws MalformedURLException {
        String resource = userImageService.getImage(userDetails.getUser().getId());
        return new UserResponseDto(userDetails.getUser(), resource);
    }

    @Transactional
    public void updateUserInfo(UserDetailsImpl userDetails, UpdateRequestDto requestDto,
        MultipartFile imageFile)
        throws IOException {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(() ->
            new IllegalArgumentException("User가 존재하지 않습니다.")
        );
        userImageService.updateImage(user, imageFile);
        user.update(requestDto);
    }

    public List<User> findContainUser(String nickname) {
        return userRepository.findByNicknameContains(nickname);
    }

    public User findUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(
            () -> new InvalidInputException("해당 User는 존재하지 않습니다.")
        );
    }
}
