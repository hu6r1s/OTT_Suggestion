package com.three.ott_suggestion.image.service;


import com.three.ott_suggestion.global.exception.InvalidInputException;
import com.three.ott_suggestion.image.entity.UserImage;
import com.three.ott_suggestion.image.repository.UserImageRepository;
import com.three.ott_suggestion.user.entity.User;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserImageService {

    @Value("${defaultImage.path}")
    private String localPath;

    private final UserImageRepository userImageRepository;

    @Value("${upload.path}")
    private String uploadPath;

    @Transactional
    public UserImage createImage(MultipartFile imageFile, User user) throws Exception {
        UserImage image = getUserImage(imageFile);
        userImageRepository.save(image);
        return image;
    }

    public String getImage(Long id) {
        UserImage image = userImageRepository.findById(id)
            .orElseThrow(() -> new InvalidInputException("프로필 이미지가 존재하지 않습니다."));
        return image.getFilePath();
    }


    @Transactional
    public void updateImage(User user, MultipartFile imageFile) throws IOException {
        UserImage image = getUserImage(imageFile);
        UserImage userImage = userImageRepository.findByUserId(user.getId())
            .orElseThrow(() -> new InvalidInputException("프로필 이미지가 존재하지 않습니다."));
        userImage.updateUserImage(image);
    }

    private UserImage getUserImage(MultipartFile imageFile) throws IOException {
        if (imageFile != null) {
            String originalFilename = imageFile.getOriginalFilename();
            String saveFileName = createSaveFileName(originalFilename);
            imageFile.transferTo(new File(getFullPath(saveFileName)));

            String filePath = uploadPath + saveFileName;

            String contentType = imageFile.getContentType();

            UserImage image = UserImage.builder()
                .fileName(originalFilename)
                .saveFileName(saveFileName)
                .contentType(contentType)
                .filePath(filePath)
                .build();
            return image;
        }
        String defaultFileName = "default.jpg";
        String defaultFilePath = localPath + defaultFileName;
        return UserImage.builder()
            .fileName(defaultFileName)
            .saveFileName("default")
            .contentType("image/png")
            .filePath(defaultFilePath)
            .build();
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
