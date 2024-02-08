package com.three.ott_suggestion.comment.repository;

import com.three.ott_suggestion.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
