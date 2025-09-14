package com.sky.movieratingservice.interfaces.restcontroller;

import com.sky.movieratingservice.domain.exception.InvalidRequestException;
import com.sky.movieratingservice.domain.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ApiErrorResponseDto> handleInvalidRequestException(InvalidRequestException ex) {
        ApiErrorResponseDto errorDto = ApiErrorResponseDto.builder()
                .errorMessage(ex.getMessage())
                .errorCode("INVALID_REQUEST")
                .details(null)
                .build();
        return ResponseEntity.badRequest().body(errorDto);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiErrorResponseDto> handleNotFoundException(NotFoundException ex) {
        ApiErrorResponseDto errorDto = ApiErrorResponseDto.builder()
                .errorMessage(ex.getMessage())
                .errorCode("NOT_FOUND")
                .details(null)
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDto);
    }
}
