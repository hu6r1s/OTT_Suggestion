package com.three.ott_suggestion.image.repository;

import com.three.ott_suggestion.image.entity.PostImage;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {

    Optional<PostImage> findByPostId(Long id);
}
