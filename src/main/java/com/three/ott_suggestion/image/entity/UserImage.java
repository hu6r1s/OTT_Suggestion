package com.three.ott_suggestion.image.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    private String saveFileName;
    private String contentType;
    private String filePath;

    public void updateUserImage(UserImage userImage) {
        this.fileName = userImage.getFileName();
        this.saveFileName = userImage.getSaveFileName();
        this.contentType = userImage.getContentType();
        this.filePath = userImage.getFilePath();
    }
}
