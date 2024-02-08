package com.three.ott_suggestion.user.dto;

import com.three.ott_suggestion.user.entity.User;
import lombok.Getter;

@Getter
public class UserResponseDto {

    private Long id;

    private String email;

    private String nickname;

    private String introduction;

    public  UserResponseDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.introduction = user.getIntroduction();
    }
}
