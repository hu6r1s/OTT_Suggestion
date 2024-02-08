package com.three.ott_suggestion.post.repository;


import com.three.ott_suggestion.post.entity.Post;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findByIdAndUserId(Long postId, Long userId);

    List<Post> findByTitle(String type);

    List<Post> findByUserId(Long id);

    Optional<Post> findPostByIdAndDeletedAtIsNull(Long postId);

    List<Post> findAllByDeletedAtIsNull();
}
