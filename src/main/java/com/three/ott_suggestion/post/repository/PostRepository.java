package com.three.ott_suggestion.post.repository;


import com.three.ott_suggestion.post.entity.Post;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByTitleContains(String title);

    List<Post> findByUserId(Long id);

    Optional<Post> findPostByIdAndDeletedAtIsNull(Long postId);

    List<Post> findAllByDeletedAtIsNullOrderByCreatedAtDesc();
}
