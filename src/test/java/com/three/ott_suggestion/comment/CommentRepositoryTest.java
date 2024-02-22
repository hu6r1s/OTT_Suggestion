package com.three.ott_suggestion.comment;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.three.ott_suggestion.comment.dto.CommentRequestDto;
import com.three.ott_suggestion.comment.entity.Comment;
import com.three.ott_suggestion.comment.repository.CommentRepository;
import com.three.ott_suggestion.post.dto.PostRequestDto;
import com.three.ott_suggestion.post.entity.Post;
import com.three.ott_suggestion.post.repository.PostRepository;
import com.three.ott_suggestion.user.entity.User;
import com.three.ott_suggestion.user.repository.UserRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("findAllByPostId 테스트")
    void findAllByPostId() {
        // given
        User user = new User(
            "test@test.com",
            "ASDasd123!!",
            "test",
            "test",
            null,
            true
        );
        userRepository.save(user);
        Post post = new Post(new PostRequestDto("title", "content"), user);
        postRepository.save(post);
        Comment comment = new Comment(new CommentRequestDto("test"), post, user);
        commentRepository.save(comment);

        // when
        List<Comment> selectComment =  commentRepository.findAllByPostId(post.getId());

        // then
        assertEquals(comment.getContent(), selectComment.get(0).getContent());
    }
}
