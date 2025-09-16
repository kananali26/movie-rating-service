package com.sky.movieratingservice.interfaces.restcontroller;

import com.sky.movieratingservice.domain.exception.InvalidRequestException;
import com.sky.movieratingservice.domain.exception.NotFoundException;
import jakarta.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ApiErrorResponseDto> handleInvalidRequestException(InvalidRequestException ex) {
        ApiErrorResponseDto errorDto = ApiErrorResponseDto.builder()
                .errorCode("INVALID_REQUEST")
                .errorMessage(ex.getMessage())
                .build();
        return ResponseEntity.badRequest().body(errorDto);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiErrorResponseDto> handleNotFoundException(NotFoundException ex) {
        ApiErrorResponseDto errorDto = ApiErrorResponseDto.builder()
                .errorCode("NOT_FOUND")
                .errorMessage(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDto);
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                               HttpHeaders headers,
                                                               HttpStatusCode status,
                                                               WebRequest request) {

        List<String> errors = new ArrayList<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiErrorResponseDto.builder()
                        .errorCode("INVALID_REQUEST")
                        .errorMessage(ex.getMessage())
                        .build());

    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponseDto> handleConstraintViolationException(ConstraintViolationException ex) {
        ApiErrorResponseDto errorDto = ApiErrorResponseDto.builder()
                .errorCode("CONSTRAINT_VIOLATION")
                .errorMessage(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

}
