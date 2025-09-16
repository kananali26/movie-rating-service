package com.sky.movieratingservice.interfaces.restcontroller.authentication;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sky.movieratingservice.openapi.interfaces.rest.dtos.LoginRequestDto;
import com.sky.movieratingservice.openapi.interfaces.rest.dtos.RegisterUserRequestDto;
import com.sky.movieratingservice.openapi.interfaces.rest.dtos.TokenResponseDto;
import com.sky.movieratingservice.utils.BaseIntegrationTest;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

class AuthenticationRestControllerIntegrationTest extends BaseIntegrationTest {

    @BeforeEach
    void setupDb() {
        executeQuery("DELETE FROM users_roles;");
        executeQuery("DELETE FROM users;");
        executeQuery("DELETE FROM roles;");
        executeQuery("INSERT INTO roles (name) VALUES ('ROLE_USER');");
    }

    @Test
    @DisplayName("Should login registered user successfully and return JWT token")
    @SneakyThrows
    void login_returnsJwt_withValidClaims() {
        // --- register ---
        String email = "test_user_1@gmail.com";
        String password = "P@ssw0rd!";

        var register = RegisterUserRequestDto.builder()
                .email(email)
                .password(password)
                .build();

        mockMvc.perform(post("/api/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(register)))
                .andExpect(status().isCreated());

        // --- login ---
        var login = LoginRequestDto.builder()
                .email(email)
                .password(password)
                .build();

        MvcResult result = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andReturn();

        TokenResponseDto tokenResponseDto = objectMapper.readValue(result.getResponse().getContentAsString(), TokenResponseDto.class);

        assertNotNull(tokenResponseDto);
        assertNotNull(tokenResponseDto.getToken());

    }

}