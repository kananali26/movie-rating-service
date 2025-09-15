package com.sky.movieratingservice.interfaces.restcontroller;

import lombok.Builder;

@Builder
public record ApiErrorResponseDto(String errorCode, String errorMessage) {
}
