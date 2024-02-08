package com.three.ott_suggestion.post.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostResponseDto {
    private Long id;
    private String title;
    private String content;
    private String nickname;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
