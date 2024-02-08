package com.three.ott_suggestion.post.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostRequestDto {
    private String title;
    private String contents;
    private String imageUrl;
}
