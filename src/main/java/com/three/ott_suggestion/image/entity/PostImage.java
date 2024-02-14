package com.three.ott_suggestion.image.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

@Builder
@Entity
@Getter
@Table(name = "post_images")
@AllArgsConstructor
@NoArgsConstructor
public class PostImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    private String saveFileName;
    private String contentType;
    private String filePath;

    public void updatePostImage(PostImage image) {
        this.fileName = image.getFileName();
        this.saveFileName = image.getSaveFileName();
        this.contentType = image.getContentType();
        this.filePath = image.getFilePath();
    }
}
