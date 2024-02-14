package com.three.ott_suggestion.follow.service;

import com.three.ott_suggestion.follow.entity.Follow;
import com.three.ott_suggestion.follow.repository.FollowRepository;
import com.three.ott_suggestion.global.exception.InvalidInputException;
import com.three.ott_suggestion.post.dto.PostResponseDto;
import com.three.ott_suggestion.post.entity.Post;
import com.three.ott_suggestion.post.repository.PostRepository;
import com.three.ott_suggestion.user.entity.User;
import com.three.ott_suggestion.user.service.UserService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class FollowService {

    private final FollowRepository followRepository;
    private final UserService userService;
    private final PostRepository postRepository;

    @Transactional
    public void createFollow(User fromUser, Long toUserId) {
        userService.findUser(toUserId);
        Follow follow = Follow.builder()
            .fromUserId(fromUser.getId())
            .toUserId(toUserId)
            .build();
        followRepository.save(follow);
    }

    @Transactional
    public void deleteFollow(User fromUser, Long toUserId) {
        userService.findUser(toUserId);
        Follow follow = followRepository.findByFromUserIdAndToUserId(fromUser.getId(), toUserId)
            .orElseThrow(
                () -> new InvalidInputException("해당 팔로우를 찾을 수 없습니다.")
            );
        followRepository.delete(follow);
    }

    public List<PostResponseDto> getAllFollowingPost(User user) {
        List<Follow> follows = followRepository.findAllByFromUserId(user.getId());
        List<Post> posts = new ArrayList<>();

        for (Follow follow : follows) {
            Long toUserId = follow.getToUserId();
            posts.addAll(postRepository.findByUserId(toUserId));
        }
        return posts.stream()
            .map(PostResponseDto::new)
            .toList();
    }
}
