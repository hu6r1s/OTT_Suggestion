package com.three.ott_suggestion.user.repository;

import com.three.ott_suggestion.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
