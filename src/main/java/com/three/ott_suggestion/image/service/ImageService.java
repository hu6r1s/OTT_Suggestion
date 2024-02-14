package com.three.ott_suggestion.image.service;

import com.three.ott_suggestion.user.entity.User;
import java.io.IOException;
import java.net.MalformedURLException;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService<T> {

    @Transactional
    T createImage(MultipartFile file) throws Exception;

    @Transactional
    void updateImage(User user, MultipartFile imageFile) throws IOException;

    String getImage(Long id) throws MalformedURLException;
}
