package com.three.ott_suggestion.follow.repository;

import com.three.ott_suggestion.follow.entity.Follow;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    Optional<Follow> findByFromUserIdAndToUserId(Long fromUserId, Long toUserId);

    List<Follow> findAllByFromUserId(Long id);
}
