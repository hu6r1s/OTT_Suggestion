package com.three.ott_suggestion.user.entity;

import com.three.ott_suggestion.global.util.TimeStamped;
import com.three.ott_suggestion.image.entity.UserImage;
import com.three.ott_suggestion.user.dto.UpdateRequestDto;
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
@Table(name = "users")
@Getter
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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserImage userImage;

    @Column(name = "verification_code", length = 64)
    private String verificationCode;

    private boolean enabled;

    public User(String email, String password, String nickname, String introduction,
            UserImage image, String verificationCode, boolean enabled) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.introduction = introduction;
        this.userImage = image;
        this.verificationCode = verificationCode;
        this.enabled = enabled;
    }

    public void update(UpdateRequestDto requestDto) {
        this.nickname = requestDto.getNickname();
        this.introduction = requestDto.getIntroduction();
    }

    public void updateImage(UserImage userImage) {
        this.userImage = userImage;
    }

    public void verify(String verificationCode, boolean enabled) {
        this.verificationCode = verificationCode;
        this.enabled = enabled;
    }
}
