package com.sky.movieratingservice.interfaces.restcontroller;

import java.util.Map;
import lombok.Builder;

@Builder
record ApiErrorResponseDto(String errorCode, String errorMessage, Map<String, String> details) {
}
