package com.sky.movieratingservice.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
public class PublicEndpointsConfiguration {

    private static final RequestMatcher[] PUBLIC_ENDPOINTS = {
            new AntPathRequestMatcher("/actuator/**", "GET"),
            new AntPathRequestMatcher("/api/v1/movies/top-rated", "GET"),
            new AntPathRequestMatcher("/api/v1/users/register", "POST"),
            new AntPathRequestMatcher("/swagger-ui/**", "GET"),
            new AntPathRequestMatcher("/swagger-ui.html", "GET"),
            new AntPathRequestMatcher("/v3/api-docs/**", "GET"),
            new AntPathRequestMatcher("/v3/api-docs.yaml", "GET"),
            new AntPathRequestMatcher("/api-docs/**", "GET"),
            new AntPathRequestMatcher("/api-docs.html", "GET"),
            new AntPathRequestMatcher("/api/v1/auth/login", "POST"),
            new AntPathRequestMatcher("/api/v1/movies", "GET")
    };

    public RequestMatcher[] getPublicEndpoints() {
        return PUBLIC_ENDPOINTS;
    }
}