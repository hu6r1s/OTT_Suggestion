package com.three.ott_suggestion.user.controller;

import com.three.ott_suggestion.global.response.CommonResponse;
import com.three.ott_suggestion.global.response.ErrorResponse;
import com.three.ott_suggestion.user.dto.SignupRequestDto;
import com.three.ott_suggestion.user.service.AuthService;
import jakarta.validation.Valid;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<CommonResponse<List<ErrorResponse>>> signup(
            @RequestBody @Valid SignupRequestDto signupRequestDto,
            BindingResult bindingResult
    ) {

        // Validation 예외처리
        if (bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            List<ErrorResponse> ErrorResponseList = new ArrayList<>();
            for (FieldError fieldError : fieldErrors) {
                ErrorResponse exceptionResponse = new ErrorResponse(fieldError.getDefaultMessage());
                ErrorResponseList.add(exceptionResponse);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(
                    CommonResponse.<List<ErrorResponse>>builder().message("회원가입 실패")
                            .data(ErrorResponseList).build());
        }

        authService.signup(signupRequestDto);

        return ResponseEntity.status(HttpStatus.OK.value()).body(
                CommonResponse.<List<ErrorResponse>>builder().message("회원가입 성공").build()
        );
    }

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
}
