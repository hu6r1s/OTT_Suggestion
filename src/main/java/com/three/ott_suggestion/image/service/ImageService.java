package com.three.ott_suggestion.image.service;

import com.three.ott_suggestion.post.entity.Post;
import com.three.ott_suggestion.user.entity.User;
import java.io.IOException;
import java.net.MalformedURLException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService<T> {

    @Transactional
    T createImage(MultipartFile file) throws Exception;

    String getImage(Long id) throws MalformedURLException;

    @Transactional
    default void updateImage(User user, MultipartFile imageFile) throws IOException {
    }

    ;

    @Transactional
    default void updateImage(Post post, MultipartFile imageFile) throws IOException {
    }

    ;
}
