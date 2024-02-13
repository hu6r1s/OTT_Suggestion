package com.three.ott_suggestion.comment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentRequestDto {

    private String content;

    @Builder
    public CommentRequestDto(String content) {
        this.content = content;
    }
}
