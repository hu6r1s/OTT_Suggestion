package com.three.ott_suggestion.image.service;

import com.three.ott_suggestion.image.repository.PostImageRepository;
import com.three.ott_suggestion.image.PostImage;
import java.io.File;
import java.net.MalformedURLException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class PostImageService implements ImageService<PostImage> {

    @Value("${upload.path}")
    private String uploadPath;

    private final PostImageRepository postImageRepository;

    @Override
    @Transactional
    public PostImage createImage(MultipartFile file) throws Exception {
        String originalFilename = file.getOriginalFilename();
        String saveFileName = createSaveFileName(originalFilename);
        file.transferTo(new File(getFullPath(saveFileName)));

        String contentType = file.getContentType();

        PostImage image = PostImage.builder()
                .fileName(originalFilename)
                .saveFileName(saveFileName)
                .contentType(contentType)
                .build();
        postImageRepository.save(image);
        return image;
    }

    private String createSaveFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

    private String getFullPath(String filename) {
        return uploadPath + filename;
    }

    @Override
    public UrlResource getImage(Long id) throws MalformedURLException {
        PostImage image = postImageRepository.findById(id).orElseThrow();
        String fileName = image.getSaveFileName();
        return new UrlResource("file:" + uploadPath + fileName);
    }
}
