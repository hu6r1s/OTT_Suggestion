package com.three.ott_suggestion.follow;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    Optional<Follow> findByFromUserIdAndToUserId(Long fromUserId, Long toUserId);
}
