package com.three.ott_suggestion.post;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.three.ott_suggestion.post.dto.PostRequestDto;
import com.three.ott_suggestion.post.entity.Post;
import com.three.ott_suggestion.post.repository.PostRepository;
import com.three.ott_suggestion.user.entity.User;
import com.three.ott_suggestion.user.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class PostRepositoryTest {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    private Post post;
    private User user;

    @BeforeEach
    private void mockPostSetup() {
        String title = "test title";
        String content = "test content";
        String email = "test@test.com";
        String password = "ASDasd123!!!";
        String nickname = "test";
        String introduction = "test";
        user = new User(email, password, nickname, introduction, null, true);
        post = new Post(new PostRequestDto(title, content), user);
        userRepository.save(user);
        postRepository.save(post);
    }
    @Test
    @DisplayName("findByUserId 테스트")
    void findByUserIdTest() {
        // given
        // when
        List<Post> selectPost = postRepository.findByUserId(1L);

        // then
        assertEquals(post.getTitle(), selectPost.get(0).getTitle());
    }

    @Test
    @DisplayName("findByTitleContains 테스트")
    void findByTitleContainsTest() {
        // given
        // when
        List<Post> selectPost = postRepository.findByTitleContains(post.getTitle());

        // then
        assertEquals(post.getTitle(), selectPost.get(0).getTitle());
    }

    @Test
    @DisplayName("findPostByIdAndDeletedAtIsNull 테스트")
    void findPostByIdAndDeletedAtIsNullTest() {
        // given
        // when
        Optional<Post> selectPost = postRepository.findPostByIdAndDeletedAtIsNull(post.getId());

        // then
        assertEquals(post.getTitle(), selectPost.get().getTitle());
    }

    @Test
    @DisplayName("findAllByDeletedAtIsNullOrderByCreatedAtDesc 테스트")
    void findAllByDeletedAtIsNullOrderByCreatedAtDescTest() {
        // given
        // when
        List<Post> selectPost = postRepository
            .findAllByDeletedAtIsNullOrderByCreatedAtDesc();

        // then
        assertEquals(post.getTitle(), selectPost.get(0).getTitle());
    }
}
