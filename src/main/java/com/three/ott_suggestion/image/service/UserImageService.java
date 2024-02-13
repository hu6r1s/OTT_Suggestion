package com.three.ott_suggestion.image.service;


import com.three.ott_suggestion.image.UserImage;
import com.three.ott_suggestion.image.repository.UserImageRepository;
import java.io.File;
import java.net.MalformedURLException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
@RequiredArgsConstructor
public class UserImageService implements ImageService<UserImage>{

    private final UserImageRepository userImageRepository;

    @Value("${upload.path}")
    private String uploadPath;
    @Override
    public UserImage createImage(MultipartFile file) throws Exception {
        String originalFilename = file.getOriginalFilename();
        String saveFileName = createSaveFileName(originalFilename);
        file.transferTo(new File(getFullPath(saveFileName)));

        String contentType = file.getContentType();

        UserImage image = UserImage.builder()
                .fileName(originalFilename)
                .saveFileName(saveFileName)
                .contentType(contentType)
                .build();
        userImageRepository.save(image);
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
        Optional<UserImage> image = userImageRepository.findById(id);
        if(image.isPresent()){
            String fileName = image.get().getSaveFileName();
            return new UrlResource("file:" + uploadPath + fileName);
        }
        return null;
    }
}
