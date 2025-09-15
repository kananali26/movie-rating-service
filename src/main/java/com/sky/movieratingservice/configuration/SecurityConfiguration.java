package com.sky.movieratingservice.configuration;

import com.sky.movieratingservice.interfaces.security.CustomAccessDeniedHandler;
import com.sky.movieratingservice.interfaces.security.JwtAuthenticationEntryPoint;
import com.sky.movieratingservice.interfaces.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    private static final RequestMatcher[] PUBLIC_ENDPOINTS = {
            new AntPathRequestMatcher("/actuator/**"),
            new AntPathRequestMatcher("/api/v1/movies/top-rated"),
            new AntPathRequestMatcher("/api/v1/users/register"),
            new AntPathRequestMatcher("/swagger-ui/**"),
            new AntPathRequestMatcher("/swagger-ui.html"),
            new AntPathRequestMatcher("/v3/api-docs/**"),
            new AntPathRequestMatcher("/api/v1/auth/login")
    };

    public RequestMatcher[] provide() {
        return PUBLIC_ENDPOINTS;
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(provide())
                        .permitAll()
                        .requestMatchers(HttpMethod.POST,   "/api/v1/movies/*/ratings").hasAuthority("RATE_MOVIE")
                        .requestMatchers(HttpMethod.DELETE,   "/api/v1/movies/*/ratings").hasAuthority("DELETE_MOVIE_RATING")
                        .requestMatchers(HttpMethod.GET,   "/api/v1/movies").permitAll()
                        .requestMatchers(HttpMethod.POST,   "/api/v1/movies").hasRole("ADMIN")
                        .anyRequest()
                        .authenticated())
                .headers(h -> h.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .formLogin(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler))
                .logout(AbstractHttpConfigurer::disable);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
