package com.three.ott_suggestion;

import com.three.ott_suggestion.post.dto.PostRequestDto;
import com.three.ott_suggestion.post.entity.Post;
import com.three.ott_suggestion.post.repository.PostRepository;
import com.three.ott_suggestion.post.service.PostService;
import com.three.ott_suggestion.user.entity.User;
import com.three.ott_suggestion.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.parameters.P;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class PostTest {

    @Autowired
    PostRepository postRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PostService postService;

    @Test
    @DisplayName("포스트 생성 Test")
    @Rollback(value = false)
    public void createPostTest() throws Exception {
        //given
        User user = new User();
        user.setEmail("1234@email.com");
        user.setIntroduction("안녕하세요");
        user.setPassword("1234");
        user.setNickname("nick");

        userRepository.save(user);

        PostRequestDto requestDto = new PostRequestDto();

        requestDto.setTitle("title");
        requestDto.setContents("contents");
        requestDto.setImageUrl("image");

        Post post = new Post(requestDto,user);

        postRepository.save(post);
        //when

        //then
    }
}
