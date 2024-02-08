package com.three.ott_suggestion.post.repository;


import com.three.ott_suggestion.post.entity.Post;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findByIdAndUserId(Long postId, Long userId);
}
