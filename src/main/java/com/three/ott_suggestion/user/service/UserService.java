package com.three.ott_suggestion.user.service;

import com.three.ott_suggestion.global.util.UserDetailsImpl;
import com.three.ott_suggestion.image.UserImage;
import com.three.ott_suggestion.image.service.ImageService;
import com.three.ott_suggestion.user.dto.UpdateRequestDto;
import com.three.ott_suggestion.user.dto.UserResponseDto;
import com.three.ott_suggestion.user.entity.User;
import com.three.ott_suggestion.user.repository.UserRepository;
import java.net.MalformedURLException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ImageService<UserImage> userImageImageService;

    public UserResponseDto getUserInfo(UserDetailsImpl userDetails) throws MalformedURLException {
        UrlResource resource = userImageImageService.getImage(userDetails.getUser().getId());
        return new UserResponseDto(userDetails.getUser(),resource);
    }

    @Transactional
    public void updateUserInfo(UserDetailsImpl userDetails, UpdateRequestDto requestDto) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(() ->
            new IllegalArgumentException("User가 존재하지 않습니다.")
        );
        user.update(requestDto);
    }

    public User findUser(String nickname){
        return userRepository.findByNickname(nickname);
    }
}
