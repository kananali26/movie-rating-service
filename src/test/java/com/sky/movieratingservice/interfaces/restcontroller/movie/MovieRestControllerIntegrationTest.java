package com.sky.movieratingservice.interfaces.restcontroller.movie;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sky.movieratingservice.openapi.interfaces.rest.dtos.MovieListResponseDto;
import com.sky.movieratingservice.utils.BaseIntegrationTest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;

class MovieRestControllerIntegrationTest extends BaseIntegrationTest {

    @BeforeEach
    void cleanupMovies() {
        String deleteQuery = "DELETE FROM movies;";
        executeQuery(deleteQuery);
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

    private void insertMovie(String name, int ratingCount, double averageRating) {
        String movieInsertQuery = """ 
                INSERT INTO movies (name, rating_count, average_rating)
                VALUES ('%s', %d, %.2f);
                """;
        executeQuery(String.format(movieInsertQuery, name, ratingCount, averageRating));
    }

}