package com.three.ott_suggestion.user.controller;

import com.three.ott_suggestion.global.response.CommonResponse;
import com.three.ott_suggestion.global.response.ErrorResponse;
import com.three.ott_suggestion.global.util.UserDetailsImpl;
import com.three.ott_suggestion.user.dto.UpdateRequestDto;
import com.three.ott_suggestion.user.dto.UserResponseDto;
import com.three.ott_suggestion.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.net.MalformedURLException;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/my")
@RequiredArgsConstructor
@Tag(name = "User", description = "유저 컨트롤러")
public class UserController {

    private final UserService userService;

    @Operation(summary = "마이페이지 조회", description = "마이페이지 정보 조회할 수 있는 API")
    @GetMapping
    public ResponseEntity<CommonResponse<UserResponseDto>> getUserInfo(
        @AuthenticationPrincipal UserDetailsImpl userDetails) throws MalformedURLException {
        return ResponseEntity.status(HttpStatus.OK.value()).body(
            CommonResponse.<UserResponseDto>builder()
                .message("회원 조회 성공")
                .data(userService.getUserInfo(userDetails)).build());
    }

    @Operation(summary = "마이페이지 수정", description = "마이페이지 정보 수정할 수 있는 API")
    @PutMapping
    public ResponseEntity<CommonResponse<List<ErrorResponse>>> update(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestPart UpdateRequestDto requestDto,
        @RequestPart(required = false) MultipartFile image,
        BindingResult bindingResult
    ) throws IOException {
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

        userService.updateUserInfo(userDetails, requestDto, image);

        return ResponseEntity.status(HttpStatus.OK.value()).body(
            CommonResponse.<List<ErrorResponse>>builder().message("수정되었습니다").build());
    }
}
