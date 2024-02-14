package com.three.ott_suggestion.like.service;

import com.three.ott_suggestion.global.exception.InvalidInputException;
import com.three.ott_suggestion.like.entity.Like;
import com.three.ott_suggestion.like.repository.LikeRepository;
import com.three.ott_suggestion.post.dto.PostResponseDto;
import com.three.ott_suggestion.post.service.PostService;
import com.three.ott_suggestion.user.entity.User;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostService postService;

    public Long countLikes(Long postId) {
        postService.findPost(postId);
        return likeRepository.countByPostId(postId);
    }

    @Transactional
    public String createLike(Long postId, User user) {
        postService.findPost(postId);
        if (likeRepository.findByUserIdAndPostId(user.getId(), postId).isPresent()) {
            throw new InvalidInputException("이미 좋아요를 눌렀습니다.");
        }
        Like like = new Like(postId, user.getId());
        likeRepository.save(like);
        return "좋아요";
    }

    @Transactional
    public String deleteLike(Long postId, User user) {
        postService.findPost(postId);
        Like like = likeRepository.findByUserIdAndPostId(user.getId(), postId).orElseThrow(
            () -> new InvalidInputException("이미 좋아요 취소를 했습니다.")
        );
        likeRepository.delete(like);
        return "좋아요 취소";
    }

    public List<PostResponseDto> getLikeTopThreePosts() {
        List<Like> topThreeLikes = highCountSorted();
        List<PostResponseDto> result = new ArrayList<>();

        for (Like like : topThreeLikes) {
            PostResponseDto postResponseDto = new PostResponseDto(
                postService.findPost(like.getPostId()));
            result.add(postResponseDto);
        }
        return result;
    }

    public List<Like> highCountSorted() {
        List<Object[]> results = likeRepository.findTop3LikedPosts();
        List<Like> likes = new ArrayList<>();
        for (Object[] result : results) {
            likes.add(new Like((Long) result[0], null));
        }
        return likes;
    }
}
