package com.three.ott_suggestion.post.service;

import com.three.ott_suggestion.global.exception.InvalidInputException;
import com.three.ott_suggestion.global.exception.InvalidPostException;
import com.three.ott_suggestion.global.exception.InvalidUserException;
import com.three.ott_suggestion.image.Image;
import com.three.ott_suggestion.image.ImageService;
import com.three.ott_suggestion.post.SearchType;
import com.three.ott_suggestion.post.dto.PostRequestDto;
import com.three.ott_suggestion.post.dto.PostResponseDto;
import com.three.ott_suggestion.post.entity.Post;
import com.three.ott_suggestion.post.repository.PostRepository;
import com.three.ott_suggestion.user.entity.User;
import com.three.ott_suggestion.user.service.UserService;
import java.net.MalformedURLException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j(topic = "service")
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;
    private final ImageService imageService;

    @Transactional
    public void createPost(String title, String contents, User user, MultipartFile file)
            throws Exception {
        log.info(user.getEmail());
        Post post = new Post(title, contents, user);
        Image image = imageService.createImage(file);
        post.createImage(image);
        postRepository.save(post);
    }

    public List<PostResponseDto> getAllPosts() {
        return postRepository.findAllByDeletedAtIsNull().stream().map(PostResponseDto::new)
                .toList();
    }

    public PostResponseDto getPost(Long postId) throws MalformedURLException {
        Post post = postRepository.findPostByIdAndDeletedAtIsNull(postId).orElseThrow(
                () -> new InvalidInputException("해당 게시글은 삭제 되었습니다.")
        );
        imageService.getFile(postId);
        return new PostResponseDto(post);
    }

    @Transactional
    public PostResponseDto updatePost(Long userId, Long postId, PostRequestDto requestDto) {
        Post post = findPost(postId);
        validateUser(userId, post);
        post.update(requestDto);
        return new PostResponseDto(post);
    }

    public List<PostResponseDto> searchPost(String type, String keyword) {
        if (type.equals(SearchType.NICKNAME.type())) {
            User user = userService.findUser(keyword);
            return postRepository.findByUserId(user.getId()).stream().map(PostResponseDto::new)
                    .toList();
        } else if (type.equals(SearchType.TITLE.type())) {
            return postRepository.findByTitle(keyword).stream().map(PostResponseDto::new).toList();
        }
        throw new InvalidInputException("query 입력값이 잘못 되었습니다.");
    }

    @Transactional
    public void deletePost(User user, Long postId) {
        Post post = findPost(postId);
        validateUser(user.getId(), post);
        post.softDelete();
    }

    private void validateUser(Long userId, Post post) {
        if (userId != post.getUser().getId()) {
            throw new InvalidUserException("해당 게시글의 유저가 아닙니다.");
        }
    }

    public Post findPost(Long postId) {
        return postRepository.findById(postId).orElseThrow(
                () -> {
                    String message = "해당 게시글이 없습니다.";
                    return new InvalidPostException(message);
                }
        );
    }
}
