package com.three.ott_suggestion.follow;

import com.three.ott_suggestion.user.entity.User;
import com.three.ott_suggestion.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class FollowService {

    private final FollowRepository followRepository;
    private final UserService userService;

    @Transactional
    public void createFollow(User fromUser, Long toUserId) {
        userService.findUser(toUserId);
        Follow follow = Follow.builder()
            .fromUserId(fromUser.getId())
            .toUserId(toUserId)
            .build();
        followRepository.save(follow);
    }
}
