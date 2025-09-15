package com.sky.movieratingservice.interfaces.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sky.movieratingservice.interfaces.restcontroller.ApiErrorResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;


    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        String requestUri = request.getRequestURI();
        requestUri = requestUri.substring(Math.max(requestUri.indexOf('/'), 0));

        ApiErrorResponseDto apiErrorResponseDto = ApiErrorResponseDto.builder()
                .errorCode("AUTHORIZATION_ERROR")
                .errorMessage("Access is forbidden to: " + requestUri)
                .build();

        response.getWriter().write(objectMapper.writeValueAsString(apiErrorResponseDto));
    }


}
