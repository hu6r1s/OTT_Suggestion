package com.three.ott_suggestion.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class UpdateRequestDto {
    private String nickname;

    private String introduction;
}
