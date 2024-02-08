package com.three.ott_suggestion.comment.entity;

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

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    public Post post;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    public User user;

}
