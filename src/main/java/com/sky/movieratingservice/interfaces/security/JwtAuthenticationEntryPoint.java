package com.sky.movieratingservice.interfaces.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sky.movieratingservice.openapi.interfaces.rest.dtos.ApiErrorResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ApiErrorResponseDto apiErrorResponseDto = ApiErrorResponseDto.builder()
                .errorCode("AUTHENTICATION_ERROR")
                .errorMessage(authException.getMessage())
                .build();

        response.getWriter().write(objectMapper.writeValueAsString(apiErrorResponseDto));
    }
}
