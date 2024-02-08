package com.three.ott_suggestion.like;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LikeRequestDto {

    private Long userId;
    private Long postId;

    public LikeRequestDto(Long userId, Long postId) {
        this.userId = userId;
        this.postId = postId;
    }
}
