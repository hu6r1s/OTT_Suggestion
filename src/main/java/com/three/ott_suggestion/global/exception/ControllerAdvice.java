package com.three.ott_suggestion.global.exception;

import com.three.ott_suggestion.global.response.CommonResponse;
import com.three.ott_suggestion.global.response.ErrorResponse;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponse<List<ErrorResponse>>> handleValidationException(
        MethodArgumentNotValidException e) {
        log.error("회원 검증 실패", e);
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        List<ErrorResponse> errorResponseList = new ArrayList<>();
        for (FieldError fieldError : fieldErrors) {
            ErrorResponse exceptionResponse = new ErrorResponse(fieldError.getDefaultMessage());
            errorResponseList.add(exceptionResponse);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(
            CommonResponse.<List<ErrorResponse>>builder().message("회원가입 실패")
                .data(errorResponseList).build());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException e) {
        log.error("인증 실패", e);
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ErrorResponse> handleInvalidInputException(InvalidInputException e) {
        log.error("잘못된 입력", e);
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleUnhandledException(RuntimeException e) {
        log.error("처리되지 않은 예외 발생", e);
        return ResponseEntity.badRequest().body("Unhandled Exception");
    }

}
