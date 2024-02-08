package com.three.ott_suggestion.post.repository;


import com.three.ott_suggestion.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

}
