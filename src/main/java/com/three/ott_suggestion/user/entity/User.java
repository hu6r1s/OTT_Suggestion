package com.three.ott_suggestion.user.entity;

import com.three.ott_suggestion.global.util.TimeStamped;
import com.three.ott_suggestion.user.dto.UpdateRequestDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(unique = true)
    private String nickname;

    @Column
    private String introduction;

    public User(String email, String password, String nickname, String introduction) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.introduction = introduction;
    }

    public void update(UpdateRequestDto requestDto) {
        this.nickname = requestDto.getNickname();
        this.introduction = requestDto.getIntroduction();
    }
}
