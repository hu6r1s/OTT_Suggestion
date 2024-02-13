package com.three.ott_suggestion.user.repository;

import com.three.ott_suggestion.user.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    List<User> findByNicknameContains(String nickname);
}