package com.three.ott_suggestion.image.entity;

import com.three.ott_suggestion.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Entity
@Getter
@Table(name = "user_images")
@AllArgsConstructor
@NoArgsConstructor
public class UserImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "file_name")
    private String fileName;
    @Column(name = "save_file_name")
    private String saveFileName;
    @Column(name = "content_type")
    private String contentType;
    @Column(name = "file_path")
    private String filePath;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public void updateUserImage(UserImage userImage) {
        this.fileName = userImage.getFileName();
        this.saveFileName = userImage.getSaveFileName();
        this.contentType = userImage.getContentType();
        this.filePath = userImage.getFilePath();
    }
}
