package com.three.ott_suggestion.post.dto;

import com.three.ott_suggestion.post.entity.Post;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class PostResponseDto {

    private Long id;
    private String title;
    private String content;
    private String nickname;
    private String imageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public PostResponseDto(Post post, String imageUrl) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.imageUrl = imageUrl;
        this.nickname = post.getUser().getNickname();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
    }

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.imageUrl = post.getPostImage().getSaveFileName();
        this.nickname = post.getUser().getNickname();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
    }
}
