package com.three.ott_suggestion.post;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.three.ott_suggestion.post.dto.PostRequestDto;
import com.three.ott_suggestion.post.entity.Post;
import com.three.ott_suggestion.post.repository.PostRepository;
import com.three.ott_suggestion.user.entity.User;
import com.three.ott_suggestion.user.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class PostRepositoryTest {
    @Autowired
    private PostRepository postRepository;

    private Post post;
    private User user;

    private void mockPostSetup() {
        String title = "test title";
        String content = "test content";
        String email = "test@test.com";
        String password = "ASDasd123!!!";
        String nickname = "test";
        String introduction = "test";
        user = new User(email, password, nickname, introduction, null, true);
        post = new Post(new PostRequestDto(title, content), user);
        postRepository.save(post);

    }
    @Test
    @DisplayName("findByUserId 테스트")
    void findByUserIdTest() {
        // given
        mockPostSetup();

        // when
        List<Post> selectPost = postRepository.findByUserId(post.getUser().getId());

        // then
        assertEquals(post.getTitle(), selectPost.get(0).getTitle());
    }


}
