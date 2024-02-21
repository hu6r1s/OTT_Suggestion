package com.three.ott_suggestion.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.three.ott_suggestion.user.entity.User;
import com.three.ott_suggestion.user.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    private User testUser;

    private void mockUserSetup() {
        String email = "test@test.com";
        String password = "ASDasd123!!!";
        String nickname = "test";
        String introduction = "test";
        testUser = new User(email, password, nickname, introduction, null, true);
    }
    @Test
    @DisplayName("findByEmail 테스트")
    void findByEmailTest() {
        // given
        mockUserSetup();
        userRepository.save(testUser);

        // when
        Optional<User> selectUser = userRepository.findByEmail(testUser.getEmail());

        // then
        assertEquals(testUser.getEmail(), selectUser.get().getEmail());
    }

    @Test
    @DisplayName("findByNicknameContains 테스트")
    void findByNicknameContainsTest() {
        // given
        mockUserSetup();
        userRepository.save(testUser);

        // when
        List<User> selectUserList = userRepository.findByNicknameContains(testUser.getNickname());

        // then
        assertEquals(testUser.getNickname(), selectUserList.get(0).getNickname());
    }

    @Test
    @DisplayName("findByVerificationCode 테스트")
    void findByVerificationCodeTest() {
        // given
        mockUserSetup();
        userRepository.save(testUser);

        // when
        User selectUser = userRepository.findByVerificationCode(testUser.getVerificationCode());

        // then
        assertEquals(testUser.getVerificationCode(), selectUser.getVerificationCode());
    }
}
