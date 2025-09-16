package com.sky.movieratingservice.interfaces.restcontroller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sky.movieratingservice.openapi.interfaces.rest.dtos.MovieListResponseDto;
import com.sky.movieratingservice.utils.BaseIntegrationTest;
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
        insertMovie("The Shawshank Redemption");

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
        insertMovie("The Shawshank Redemption");
        insertMovie("The Godfather");
        insertMovie("The Dark Knight");

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
        insertMovie("The Shawshank Redemption");
        insertMovie("The Godfather");
        insertMovie("The Dark Knight");
        insertMovie("Pulp Fiction");
        insertMovie("Fight Club");
        insertMovie("Goodfellas");
        insertMovie("The Green Mile");
        insertMovie("Parasite");
        insertMovie("Mad Max: Fury Road");
        insertMovie("Amélie");
        insertMovie("Spirited Away");

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

    private void insertMovie(String name) {
        String movieInsertQuery = """ 
                INSERT INTO movies (name)
                VALUES ('%s');
                """;
        executeQuery(String.format(movieInsertQuery, name));
    }

}