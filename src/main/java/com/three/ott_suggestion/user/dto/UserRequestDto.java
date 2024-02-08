package com.three.ott_suggestion.user.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UserRequestDto {
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Email 형식에 맞도록 입력해 주세요.")
    private String email;

    @Size(min = 8, max = 15, message = "Password는 8자 이상 15자 이하이어야 합니다.")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).*$", message = "Password는 적어도 알파벳 대소문자(a~z, A~Z), 숫자(0~9), 특수 문자(@, #, $, %, ^, &, +, =, !) 하나 이상씩 구성되어야 합니다.")
    private String password;
}