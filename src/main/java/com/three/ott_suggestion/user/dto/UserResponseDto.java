package com.three.ott_suggestion.user.dto;

import com.three.ott_suggestion.user.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.core.io.UrlResource;

@Getter
public class UserResponseDto {

    private Long id;

    private String email;

    private String nickname;

    private String introduction;

    private String imgUrl;

    public UserResponseDto(User user, String resource) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.introduction = user.getIntroduction();
        this.imgUrl = resource;
    }
}
