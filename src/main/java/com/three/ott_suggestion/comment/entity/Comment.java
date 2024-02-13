package com.three.ott_suggestion.comment.entity;

import com.three.ott_suggestion.comment.dto.CommentRequestDto;
import com.three.ott_suggestion.global.util.TimeStamped;
import com.three.ott_suggestion.post.entity.Post;
import com.three.ott_suggestion.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "comments")
@NoArgsConstructor
public class Comment extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content")
    private String content;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt = null;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    public Post post;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    public User user;

    public Comment(CommentRequestDto requestDto, Post post, User user) {
        this.content = requestDto.getContent();
        this.post = post;
        this.user = user;
    }

    public void update(CommentRequestDto requestDto) {
        this.content = requestDto.getContent();
    }

    public void softDelete() {
        this.deletedAt = LocalDateTime.now();
    }
}
