package com.three.ott_suggestion.like;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByUserIdAndPostId(Long userId, Long postId);

    Long countByPostId(Long postId);

    @Query("SELECT l.postId, COUNT(DISTINCT l.userId) AS likeCount " +
            "FROM Like l " +
            "GROUP BY l.postId " +
            "ORDER BY likeCount DESC LIMIT 3"
    )
    List<Object[]> findTop3LikedPosts();
}

