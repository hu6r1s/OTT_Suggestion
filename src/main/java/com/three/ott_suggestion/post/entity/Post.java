package com.three.ott_suggestion.post.entity;

import com.three.ott_suggestion.global.util.TimeStamped;
import com.three.ott_suggestion.post.dto.PostRequestDto;
import com.three.ott_suggestion.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Table(name = "posts")
@Entity
@NoArgsConstructor
@SQLRestriction("deleted_at is NULL")
public class Post extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column
    private String image;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    public User user;

    public Post(PostRequestDto requestDto, User user) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContents();
        this.image = requestDto.getImageUrl();
        this.user = user;
    }

    public void update(PostRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContents();
        this.image = requestDto.getImageUrl();
    }

    public void softDelete(){
        this.deletedAt = LocalDateTime.now();
    }
}



