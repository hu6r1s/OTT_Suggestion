package com.three.ott_suggestion.image.service;

import com.three.ott_suggestion.global.exception.InvalidInputException;
import com.three.ott_suggestion.image.entity.PostImage;
import com.three.ott_suggestion.image.repository.PostImageRepository;
import com.three.ott_suggestion.post.entity.Post;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
        PostImage image = getPostImage(file);
        postImageRepository.save(image);
        return image;
    }

    @Override
    public String getImage(Long id) throws MalformedURLException {
        PostImage image = postImageRepository.findById(id)
                .orElseThrow(() -> new InvalidInputException("게시물 이미지가 존재하지 않습니다"));
        return image.getFilePath();
    }

    @Transactional
    @Override
    public void updateImage(Post post, MultipartFile imageFile) throws IOException {
        PostImage image = getPostImage(imageFile);
        PostImage postImage = postImageRepository.findById(post.getPostImage().getId())
                .orElseThrow(() -> new InvalidInputException("게시물 이미지가 존재하지 않습니다"));
        postImage.updatePostImage(image);
    }

    private PostImage getPostImage(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String saveFileName = createSaveFileName(originalFilename);
        file.transferTo(new File(getFullPath(saveFileName)));
        String filePath = uploadPath + saveFileName;

        String contentType = file.getContentType();

        PostImage image = PostImage.builder()
                .fileName(originalFilename)
                .saveFileName(saveFileName)
                .contentType(contentType)
                .filePath(filePath)
                .build();
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


}
