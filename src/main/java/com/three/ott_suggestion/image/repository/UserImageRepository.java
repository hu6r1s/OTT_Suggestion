package com.three.ott_suggestion.image.repository;

import com.three.ott_suggestion.image.entity.UserImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserImageRepository extends JpaRepository<UserImage, Long> {

}
