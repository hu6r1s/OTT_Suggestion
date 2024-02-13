package com.three.ott_suggestion.user.repository;

import com.three.ott_suggestion.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByVerificationCode(String verificationCode);

    Optional<User> findByEmail(String email);

}