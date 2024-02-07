package com.three.ott_suggestion.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SignupRequestDto {

    @NotBlank
    private String email;

    @NotBlank
    private String password;

}
