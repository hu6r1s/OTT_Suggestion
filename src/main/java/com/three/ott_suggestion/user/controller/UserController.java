package com.three.ott_suggestion.user.controller;

import com.three.ott_suggestion.global.response.CommonResponse;
import com.three.ott_suggestion.global.response.ErrorResponse;
import com.three.ott_suggestion.global.util.UserDetailsImpl;
import com.three.ott_suggestion.user.dto.UpdateRequestDto;
import com.three.ott_suggestion.user.dto.UserResponseDto;
import com.three.ott_suggestion.user.service.UserService;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/my")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<CommonResponse<UserResponseDto>> getUserInfo(
        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return ResponseEntity.status(HttpStatus.OK.value()).body(
            CommonResponse.<UserResponseDto>builder()
                .message("회원 조회 성공")
                .data(userService.getUserInfo(userDetails)).build());
    }

    @PutMapping
    public ResponseEntity<CommonResponse<List<ErrorResponse>>> update(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @Valid @RequestBody UpdateRequestDto requestDto,
        BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            List<ErrorResponse> ErrorResponseList = new ArrayList<>();
            for (FieldError fieldError : fieldErrors) {
                ErrorResponse exceptionResponse = new ErrorResponse(fieldError.getDefaultMessage());
                ErrorResponseList.add(exceptionResponse);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(
                CommonResponse.<List<ErrorResponse>>builder().message("수정을 실패했습니다.")
                    .data(ErrorResponseList).build());
        }

        userService.updateUserInfo(userDetails, requestDto);

        return ResponseEntity.status(HttpStatus.OK.value()).body(
            CommonResponse.<List<ErrorResponse>>builder().message("수정되었습니다").build());
    }
}
