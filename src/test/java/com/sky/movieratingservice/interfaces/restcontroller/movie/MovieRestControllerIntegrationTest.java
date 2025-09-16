package com.sky.movieratingservice.interfaces.restcontroller.movie;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.sky.movieratingservice.openapi.interfaces.rest.dtos.CreateMovieRequestDto;
import com.sky.movieratingservice.openapi.interfaces.rest.dtos.LoginRequestDto;
import com.sky.movieratingservice.openapi.interfaces.rest.dtos.MovieListResponseDto;
import com.sky.movieratingservice.openapi.interfaces.rest.dtos.TokenResponseDto;
import com.sky.movieratingservice.utils.BaseIntegrationTest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MvcResult;

class MovieRestControllerIntegrationTest extends BaseIntegrationTest {

    @BeforeEach
    void cleanupMovies() {
        executeQuery("DELETE FROM users_roles;");
        executeQuery("DELETE FROM users;");
        executeQuery("DELETE FROM roles WHERE name = 'ROLE_ADMIN';");
        executeQuery("DELETE FROM roles WHERE name = 'ROLE_USER';");
        executeQuery("INSERT INTO roles (name) VALUES ('ROLE_ADMIN');");
        executeQuery("INSERT INTO roles (name) VALUES ('ROLE_USER');");
        executeQuery("DELETE FROM movies;");
    }

    @Test
    @DisplayName("Should return all movies when no name filter is applied")
    @SneakyThrows
    void getMovies_shouldReturnAllMovies_whenNoNameProvided() {
        insertMovie("The Shawshank Redemption", 1, 9.3);

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/movies"))
                .andExpect(status().isOk())
                .andReturn();

        MovieListResponseDto movieListResponseDto = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), MovieListResponseDto.class);

        assertNotNull(movieListResponseDto);
        assertEquals(1, movieListResponseDto.getMovies().size());
        assertEquals("The Shawshank Redemption", movieListResponseDto.getMovies().getFirst().getName());
        assertEquals(1, movieListResponseDto.getPaginationInfo().getTotalRecords());
        assertEquals(0, movieListResponseDto.getPaginationInfo().getPageNumber());
        assertEquals(10, movieListResponseDto.getPaginationInfo().getPageSize());
    }

    @Test
    @DisplayName("Should return filtered movies when name filter is applied")
    @SneakyThrows
    void getMovies_shouldReturnFilteredMovies_whenNameProvided() {
        insertMovie("The Shawshank Redemption", 1, 9.3);
        insertMovie("The Godfather", 2, 9.2);
        insertMovie("The Dark Knight", 1, 9.0);

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/movies")
                        .param("name", "The Shawshank Redemption"))
                .andExpect(status().isOk())
                .andReturn();

        MovieListResponseDto movieListResponseDto = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), MovieListResponseDto.class);

        assertNotNull(movieListResponseDto);
        assertEquals(1, movieListResponseDto.getMovies().size());
        assertEquals(1, movieListResponseDto.getPaginationInfo().getTotalRecords());
        assertEquals(0, movieListResponseDto.getPaginationInfo().getPageNumber());
        assertEquals(10, movieListResponseDto.getPaginationInfo().getPageSize());
    }


    @Test
    @DisplayName("Should return correct pagination when specific page parameters provided")
    @SneakyThrows
    void getMovies_shouldReturnCorrectPagination_whenSpecificPageParamsProvided() {
        insertMovie("The Shawshank Redemption", 1, 9.3);
        insertMovie("The Godfather", 1, 9.2);
        insertMovie("The Dark Knight", 1, 9.0);
        insertMovie("Pulp Fiction", 2, 8.9);
        insertMovie("Fight Club", 3, 8.8);
        insertMovie("Goodfellas", 2, 8.7);
        insertMovie("The Green Mile", 1, 8.6);
        insertMovie("Parasite", 2, 8.6);
        insertMovie("Mad Max: Fury Road", 1, 8.1);
        insertMovie("Amélie", 1, 8.3);
        insertMovie("Spirited Away", 1, 8.5);

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/movies")
                        .param("pageNumber", "0")
                        .param("pageSize", "5"))
                .andExpect(status().isOk())
                .andReturn();

        MovieListResponseDto movieListResponseDto = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), MovieListResponseDto.class);

        assertNotNull(movieListResponseDto);
        assertEquals(5, movieListResponseDto.getMovies().size());
        assertEquals(11, movieListResponseDto.getPaginationInfo().getTotalRecords());
        assertEquals(0, movieListResponseDto.getPaginationInfo().getPageNumber());
        assertEquals(5, movieListResponseDto.getPaginationInfo().getPageSize());
    }

    @Test
    @DisplayName("Should return top rated movies when limit is provided")
    @SneakyThrows
    void getTopRatedMovies_shouldReturnTopRatedMovies_whenLimitProvided() {
        insertMovie("The Shawshank Redemption", 2, 10);
        insertMovie("Pulp Fiction", 1, 8.0);
        insertMovie("The Godfather", 3, 9.5);

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/movies/top-rated")
                        .param("limit", "3"))
                .andExpect(status().isOk())
                .andReturn();

        MovieListResponseDto movieListResponseDto = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), MovieListResponseDto.class);

        assertNotNull(movieListResponseDto);
        assertEquals(3, movieListResponseDto.getMovies().size());

        assertEquals("The Shawshank Redemption", movieListResponseDto.getMovies().getFirst().getName());
        assertEquals(BigDecimal.valueOf(10).setScale(2, RoundingMode.HALF_UP), movieListResponseDto.getMovies().getFirst().getAverageRating().setScale(2, RoundingMode.HALF_UP));
        assertEquals(2, movieListResponseDto.getMovies().getFirst().getRatingCount());

        assertEquals("The Godfather", movieListResponseDto.getMovies().get(1).getName());
        assertEquals(BigDecimal.valueOf(9.5).setScale(2, RoundingMode.HALF_UP), movieListResponseDto.getMovies().get(1).getAverageRating().setScale(2, RoundingMode.HALF_UP));
        assertEquals(3, movieListResponseDto.getMovies().get(1).getRatingCount());

        assertEquals("Pulp Fiction", movieListResponseDto.getMovies().get(2).getName());
        assertEquals(BigDecimal.valueOf(8).setScale(2, RoundingMode.HALF_UP), movieListResponseDto.getMovies().get(2).getAverageRating().setScale(2, RoundingMode.HALF_UP));
        assertEquals(1, movieListResponseDto.getMovies().get(2).getRatingCount());
    }


    @Test
    @DisplayName("Should create movie when user has admin role, returns 201")
    @SneakyThrows
    void createMovie_requiresAdmin_andReturns201() {
        // ---- 1) Insert user directly in DB with bcrypt-encoded password ----
        String email = "king_admin06@example.com";
        String rawPassword = "Password#123";
        String encoded = new BCryptPasswordEncoder().encode(rawPassword);

        executeQuery("""
            INSERT INTO users (email, password)
            VALUES ('%s', '%s');
        """.formatted(email, encoded));

        // Fetch user id
        List<Map<String, Object>> userRows = fetchDbQueryResult("""
            SELECT id AS id FROM users WHERE email = '%s'
        """.formatted(email));

        assertEquals(1, userRows.size());

        long userId = ((Number) userRows.getFirst().get("id")).longValue();

        // Fetch role id for ROLE_ADMIN
        List<Map<String, Object>> roleRows = fetchDbQueryResult("""
            SELECT id AS id FROM roles WHERE name = 'ROLE_ADMIN'
        """);
        assertEquals(1, roleRows.size());
        long roleId = ((Number) roleRows.getFirst().get("id")).longValue();

        // Link user ↔ ROLE_ADMIN
        executeQuery("""
            INSERT INTO users_roles (user_id, role_id)
            VALUES (%d, %d);
        """.formatted(userId, roleId));

        // ---- 2) Login to obtain JWT ----
        var loginReq = LoginRequestDto.builder()
                .email(email)
                .password(rawPassword)
                .build();

        MvcResult loginResult = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginReq)))
                .andExpect(status().isOk())
                .andReturn();

        TokenResponseDto tokenResponseDto = objectMapper.readValue(loginResult.getResponse().getContentAsString(), TokenResponseDto.class);

        assertNotNull(tokenResponseDto);
        assertNotNull(tokenResponseDto.getToken());

        // ---- 3) Call create movie with Bearer token ----
        var createReq = CreateMovieRequestDto.builder()
                .name("Sky! The Movie")
                .build();

        mockMvc.perform(post("/api/v1/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenResponseDto.getToken())
                        .content(objectMapper.writeValueAsString(createReq)))
                .andExpect(status().isCreated());

        List<Map<String, Object>> movieRows = fetchDbQueryResult("""
        SELECT id AS id
        FROM movies
        WHERE name = 'Sky! The Movie'
        """);
        assertEquals(1, movieRows.size(), "Exactly one movie row expected");
        Object idObj = movieRows.getFirst().get("id");
        assertNotNull(idObj, "Movie id must not be null");
    }

    @Test
    @DisplayName("Should throw 403 FORBIDDEN when user trying to create movie with only USER role")
    @SneakyThrows
    void createMovieThrowExceptionWhenUserRole() {
        // ---- 1) Insert user directly in DB with bcrypt-encoded password ----
        String email = "king_admin07@example.com";
        String rawPassword = "Password#123";
        String encoded = new BCryptPasswordEncoder().encode(rawPassword);

        executeQuery("""
            INSERT INTO users (email, password)
            VALUES ('%s', '%s');
        """.formatted(email, encoded));

        // Fetch user id
        List<Map<String, Object>> userRows = fetchDbQueryResult("""
            SELECT id AS id FROM users WHERE email = '%s'
        """.formatted(email));

        assertEquals(1, userRows.size());

        long userId = ((Number) userRows.getFirst().get("id")).longValue();

        // Fetch role id for ROLE_ADMIN
        List<Map<String, Object>> roleRows = fetchDbQueryResult("""
            SELECT id AS id FROM roles WHERE name = 'ROLE_USER'
        """);
        assertEquals(1, roleRows.size());
        long roleId = ((Number) roleRows.getFirst().get("id")).longValue();

        // Link user ↔ ROLE_ADMIN
        executeQuery("""
            INSERT INTO users_roles (user_id, role_id)
            VALUES (%d, %d);
        """.formatted(userId, roleId));

        // ---- 2) Login to obtain JWT ----
        var loginReq = LoginRequestDto.builder()
                .email(email)
                .password(rawPassword)
                .build();

        MvcResult loginResult = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginReq)))
                .andExpect(status().isOk())
                .andReturn();

        TokenResponseDto tokenResponseDto = objectMapper.readValue(loginResult.getResponse().getContentAsString(), TokenResponseDto.class);

        assertNotNull(tokenResponseDto);
        assertNotNull(tokenResponseDto.getToken());

        // ---- 3) Call create movie with Bearer token ----
        var createReq = CreateMovieRequestDto.builder()
                .name("Sky! The Movie")
                .build();

        mockMvc.perform(post("/api/v1/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenResponseDto.getToken())
                        .content(objectMapper.writeValueAsString(createReq)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.errorCode").value("AUTHORIZATION_ERROR"))
                .andExpect(jsonPath("$.errorMessage").value("Access is forbidden to: /api/v1/movies"));
    }


    private void insertMovie(String name, int ratingCount, double averageRating) {
        String movieInsertQuery = """ 
                INSERT INTO movies (name, rating_count, average_rating)
                VALUES ('%s', %d, %.2f);
                """;
        executeQuery(String.format(movieInsertQuery, name, ratingCount, averageRating));
    }

}