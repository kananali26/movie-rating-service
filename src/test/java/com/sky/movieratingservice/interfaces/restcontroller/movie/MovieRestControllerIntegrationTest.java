package com.sky.movieratingservice.interfaces.restcontroller.movie;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import com.sky.movieratingservice.openapi.interfaces.rest.dtos.CreateMovieRequestDto;
import com.sky.movieratingservice.openapi.interfaces.rest.dtos.LoginRequestDto;
import com.sky.movieratingservice.openapi.interfaces.rest.dtos.MovieListResponseDto;
import com.sky.movieratingservice.openapi.interfaces.rest.dtos.RateMovieRequestDto;
import com.sky.movieratingservice.openapi.interfaces.rest.dtos.RegisterUserRequestDto;
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
        executeQuery("DELETE FROM ratings;");
        executeQuery("DELETE FROM users_roles;");
        executeQuery("DELETE FROM users;");
        executeQuery("DELETE FROM roles_privileges;");
        executeQuery("DELETE FROM privileges;");
        executeQuery("DELETE FROM roles;");
        executeQuery("DELETE FROM movies;");

        executeQuery("INSERT INTO roles (name) VALUES ('ROLE_ADMIN');");
        executeQuery("INSERT INTO roles (name) VALUES ('ROLE_USER');");
        executeQuery("INSERT INTO privileges (name) VALUES ('RATE_MOVIE');");
        executeQuery("INSERT INTO privileges (name) VALUES ('DELETE_MOVIE_RATING');");

        Long roleUserId = getRowId("SELECT id AS id FROM roles WHERE name = 'ROLE_USER'");
        Long rateMoviePrivilegeId = getRowId("SELECT id AS id FROM privileges WHERE name = 'RATE_MOVIE'");
        Long deletePrivilegeId = getRowId("SELECT id AS id FROM privileges WHERE name = 'DELETE_MOVIE_RATING'");
        executeQuery("INSERT INTO roles_privileges (role_id, privilege_id) VALUES (%d, %d);".formatted(roleUserId, rateMoviePrivilegeId));
        executeQuery("INSERT INTO roles_privileges (role_id, privilege_id) VALUES (%d, %d);".formatted(roleUserId, deletePrivilegeId));
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

    @Test
    @DisplayName("POST /api/v1/movies/{id}/ratings should succeed for a user with RATE_MOVIE privilege")
    @SneakyThrows
    void rateMovie_withRateMoviePrivilege_returns200_andPersists() {
        // ---- Admin user (direct DB insert) to create a movie ----
        String adminEmail = "my_admin_007@example.com";
        String adminRawPassword = "Admin#123";
        String adminEncoded = new BCryptPasswordEncoder().encode(adminRawPassword);

        executeQuery(("INSERT INTO users (email, password) VALUES ('%s', '%s');")
                .formatted(adminEmail, adminEncoded));

        Long adminId = getRowId(("SELECT id AS id FROM users WHERE email = '%s'").formatted(adminEmail));
        Long roleAdminId = getRowId("SELECT id AS id FROM roles WHERE name = 'ROLE_ADMIN'");
        executeQuery(("INSERT INTO users_roles (user_id, role_id) VALUES (%d, %d);")
                .formatted(adminId, roleAdminId));

        // Login as admin
        var adminLogin = LoginRequestDto.builder()
                .email(adminEmail)
                .password(adminRawPassword)
                .build();

        MvcResult adminLoginRes = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adminLogin)))
                .andExpect(status().isOk())
                .andReturn();

        TokenResponseDto adminTokenResponseDto = objectMapper.readValue(adminLoginRes.getResponse().getContentAsString(), TokenResponseDto.class);

        assertNotNull(adminTokenResponseDto);
        assertNotNull(adminTokenResponseDto.getToken());

        String adminToken = adminTokenResponseDto.getToken();

        // Create a movie as admin
        var createMovie = CreateMovieRequestDto.builder().name("Heat").build();
        mockMvc.perform(post("/api/v1/movies")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createMovie)))
                .andExpect(status().isCreated());

        Long movieId = getRowId("SELECT id AS id FROM movies WHERE name = 'Heat'");
        assertNotNull(movieId);

        // ---- Regular user (via API) who has ROLE_USER (mapped to RATE_MOVIE) ----
        String userEmail = "my_user_007@example.com";
        String userPassword = "User#12345";
        var register = RegisterUserRequestDto.builder()
                .email(userEmail)
                .password(userPassword)
                .build();

        mockMvc.perform(post("/api/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(register)))
                .andExpect(status().isCreated());

        // Login regular user
        var userLogin = LoginRequestDto.builder()
                .email(userEmail)
                .password(userPassword)
                .build();

        MvcResult userLoginRes = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userLogin)))
                .andExpect(status().isOk())
                .andReturn();

        TokenResponseDto userTokenResponseDto = objectMapper.readValue(userLoginRes.getResponse().getContentAsString(), TokenResponseDto.class);

        assertNotNull(userTokenResponseDto);
        assertNotNull(userTokenResponseDto.getToken());

        String userToken = userTokenResponseDto.getToken();

        // ---- Rate the movie ----
        var rateReq = RateMovieRequestDto.builder().value(9).build();

        mockMvc.perform(post("/api/v1/movies/" + movieId + "/ratings")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rateReq)))
                .andExpect(status().isOk());

        // ---- Verify DB: rating row exists ----
        Long userId = getRowId(("SELECT id AS id FROM users WHERE email = '%s'").formatted(userEmail));

        List<Map<String, Object>> ratingRows = fetchDbQueryResult(("""
            SELECT r.id AS id, r.rating AS rating
            FROM ratings r
            WHERE r.movie_id = %d AND r.user_id = %d
        """).formatted(movieId, userId));

        assertEquals(1, ratingRows.size(), "Exactly one rating row expected");
        var row = ratingRows.getFirst();
        assertNotNull(row.get("id"));
        assertEquals(9, ((Number) row.get("rating")).intValue(), "Stored rating must match request value");
    }

    @Test
    @DisplayName("DELETE /api/v1/movies/{id}/ratings should remove rating for a user with DELETE_MOVIE_RATING privilege")
    @SneakyThrows
    void deleteRating_withDeletePrivilege_removesRow() {
        // ---- Admin user (direct DB insert) to create a movie ----
        String adminEmail = "admin_sky_1_089@example.com";
        String adminRawPassword = "Admin#123";
        String adminEncoded = new BCryptPasswordEncoder().encode(adminRawPassword);

        executeQuery(("INSERT INTO users (email, password) VALUES ('%s', '%s');")
                .formatted(adminEmail, adminEncoded));

        Long adminId = getRowId(("SELECT id AS id FROM users WHERE email = '%s'").formatted(adminEmail));
        Long roleAdminId = getRowId("SELECT id AS id FROM roles WHERE name = 'ROLE_ADMIN'");
        executeQuery(("INSERT INTO users_roles (user_id, role_id) VALUES (%d, %d);")
                .formatted(adminId, roleAdminId));

        // Login as admin
        var adminLogin = LoginRequestDto.builder().email(adminEmail).password(adminRawPassword).build();
        MvcResult adminLoginRes = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adminLogin)))
                .andExpect(status().isOk())
                .andReturn();

        TokenResponseDto adminTokenResponseDto = objectMapper.readValue(adminLoginRes.getResponse().getContentAsString(), TokenResponseDto.class);

        String adminToken = adminTokenResponseDto.getToken();


        // Create a movie as admin
        var createMovie = CreateMovieRequestDto.builder().name("Collateral").build();
        mockMvc.perform(post("/api/v1/movies")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createMovie)))
                .andExpect(status().isCreated());
        Long movieId = getRowId("SELECT id AS id FROM movies WHERE name = 'Collateral'");
        assertNotNull(movieId);

        // ---- Regular user (via API) who has ROLE_USER with required privileges ----
        String userEmail = "user_s_k_y_1_9_7@example.com";
        String userPassword = "User#12345";
        var register = RegisterUserRequestDto.builder().email(userEmail).password(userPassword).build();

        mockMvc.perform(post("/api/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(register)))
                .andExpect(status().isCreated());

        // Login regular user
        var userLogin = LoginRequestDto.builder().email(userEmail).password(userPassword).build();
        MvcResult userLoginRes = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userLogin)))
                .andExpect(status().isOk())
                .andReturn();

        TokenResponseDto userTokenResponseDto = objectMapper.readValue(userLoginRes.getResponse().getContentAsString(), TokenResponseDto.class);

        String userToken = userTokenResponseDto.getToken();

        // ---- Rate the movie (so there's something to delete) ----
        var rateReq = RateMovieRequestDto.builder().value(7).build();
        mockMvc.perform(post("/api/v1/movies/" + movieId + "/ratings")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rateReq)))
                .andExpect(status().isOk());

        Long userId = getRowId(("SELECT id AS id FROM users WHERE email = '%s'").formatted(userEmail));

        List<Map<String, Object>> beforeRows = fetchDbQueryResult(("""
            SELECT COUNT(*) AS cnt FROM ratings
            WHERE movie_id = %d AND user_id = %d
        """).formatted(movieId, userId));
        assertEquals(1, ((Number) beforeRows.getFirst().get("cnt")).intValue(), "rating must exist before delete");


        // ---- Delete the rating ----
        mockMvc.perform(delete("/api/v1/movies/" + movieId + "/ratings")
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk());

        // ---- Verify rating row is gone ----
        List<Map<String, Object>> afterRows = fetchDbQueryResult(("""
            SELECT COUNT(*) AS cnt FROM ratings
            WHERE movie_id = %d AND user_id = %d
        """).formatted(movieId, userId));
        assertEquals(0, ((Number) afterRows.getFirst().get("cnt")).intValue(), "rating must be deleted");
    }


    private void insertMovie(String name, int ratingCount, double averageRating) {
        String movieInsertQuery = """ 
                INSERT INTO movies (name, rating_count, average_rating, version)
                VALUES ('%s', %d, %.2f, 0);
                """;
        executeQuery(String.format(movieInsertQuery, name, ratingCount, averageRating));
    }

    private Long getRowId(String sqlSelect) {
        List<Map<String, Object>> rows = fetchDbQueryResult(sqlSelect);
        assertEquals(1, rows.size(), "Expected exactly 1 row for: " + sqlSelect);
        Object id = rows.getFirst().get("id");
        assertNotNull(id, "id must not be null for: " + sqlSelect);
        return ((Number) id).longValue();
    }
}
