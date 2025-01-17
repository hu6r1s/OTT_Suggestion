package com.three.ott_suggestion.user.controller;

import com.three.ott_suggestion.global.response.CommonResponse;
import com.three.ott_suggestion.global.response.ErrorResponse;
import com.three.ott_suggestion.user.dto.SignupRequestDto;
import com.three.ott_suggestion.user.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Auth", description = "인증 컨트롤러")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "회원가입", description = "회원가입 할 수 있는 API")
    @PostMapping("/signup")
    public ResponseEntity<CommonResponse<List<ErrorResponse>>> signup(
        @RequestBody @Valid SignupRequestDto signupRequestDto,
        HttpServletRequest request
    )
        throws UnsupportedEncodingException, MessagingException {
        authService.signup(signupRequestDto, getSiteURL(request));

        return ResponseEntity.status(HttpStatus.OK.value()).body(
            CommonResponse.<List<ErrorResponse>>builder().message("회원가입 성공").build()
        );
    }

    @Operation(summary = "이메일 인증", description = "이메일 인증할 수 있는 API")
    @GetMapping("/verify")
    public ResponseEntity<CommonResponse<Void>> verifyUser(@RequestParam String code) {
        if (authService.verify(code)) {
            return ResponseEntity.status(HttpStatus.OK.value()).body(
                CommonResponse.<Void>builder().message("이메일 인증 완료").build()
            );
        } else {
            throw new RuntimeException();
        }
    }

    @Operation(summary = "로그아웃", description = "로그아웃 할 수 있는 API")
    @PostMapping("/logout")
    public ResponseEntity<CommonResponse<Void>> logout(
        @AuthenticationPrincipal UserDetails userDetails) {
        try {
            authService.logout(userDetails);
            return ResponseEntity.status(HttpStatus.OK.value()).body(
                CommonResponse.<Void>builder().message("Refresh Token 제거").build()
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }
}
