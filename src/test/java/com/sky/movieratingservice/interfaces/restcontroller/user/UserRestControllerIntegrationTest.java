package com.sky.movieratingservice.interfaces.restcontroller.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sky.movieratingservice.openapi.interfaces.rest.dtos.RegisterUserRequestDto;
import com.sky.movieratingservice.utils.BaseIntegrationTest;
import java.util.List;
import java.util.Map;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class UserRestControllerIntegrationTest extends BaseIntegrationTest {

    @BeforeEach
    void cleanUp() {
        executeQuery("DELETE FROM users_roles;");
        executeQuery("DELETE FROM users;");
        executeQuery("DELETE FROM roles;");
        executeQuery("INSERT INTO roles (name) VALUES ('ROLE_USER');");
    }

    @Test
    @DisplayName("Should register a new user successfully")
    @SneakyThrows
    void testRegisterUser() {
        String email = "";
        String password = "password123";
        RegisterUserRequestDto request = RegisterUserRequestDto.builder().email(email).password(password).build();

        mockMvc.perform(post("/api/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        List<Map<String, Object>> userRows = fetchDbQueryResult("""
            SELECT id AS id, email AS email, password AS password
            FROM users
            WHERE email = '%s'
            """.formatted(email));

        assertEquals(1, userRows.size(), "Exactly one user row expected");
        Map<String, Object> row = userRows.getFirst();

        long userId = ((Number) row.get("id")).longValue();
        String dbEmail = (String) row.get("email");
        String dbPassword = (String) row.get("password");

        assertEquals(email, dbEmail);
        assertNotNull(dbPassword);
        assertNotEquals(password, dbPassword, "Password must be encoded (not equal to raw)");

        List<Map<String, Object>> linkRows = fetchDbQueryResult("""
            SELECT COUNT(*) AS cnt
            FROM users_roles ur
            JOIN roles r ON r.id = ur.role_id
            WHERE ur.user_id = %d AND r.name = 'ROLE_USER'
            """.formatted(userId));

        assertEquals(1, linkRows.size());
        assertEquals(1L, ((Number) linkRows.getFirst().get("cnt")).longValue(), "User should have ROLE_USER");
    }
}
