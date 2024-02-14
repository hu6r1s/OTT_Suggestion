package com.three.ott_suggestion.comment.dto;

import com.three.ott_suggestion.comment.entity.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentResponseDto {

    String content;

    public CommentResponseDto(Comment comment) {
        this.content = comment.getContent();
    }
}
