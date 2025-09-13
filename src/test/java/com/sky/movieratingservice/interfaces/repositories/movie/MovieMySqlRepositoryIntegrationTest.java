package com.sky.movieratingservice.interfaces.repositories.movie;

import static org.junit.jupiter.api.Assertions.*;

import com.sky.movieratingservice.utils.BaseIntegrationTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MovieMySqlRepositoryIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private MovieMySqlRepository movieMySqlRepository;

    @AfterEach
    void cleanupMovies() {
        String deleteQuery = "DELETE FROM movie;";
        executeQuery(deleteQuery);
    }

    @Test
    @DisplayName("Should fetch paginated movies when no filter is applied")
    void testGetMoviesWithoutFilter() {
        insertMovie("The Shawshank Redemption");
        insertMovie("The Godfather");
        insertMovie("The Dark Knight");
        insertMovie("Pulp Fiction");
        insertMovie("Fight Club");
        insertMovie("Goodfellas");
        insertMovie("The Green Mile");

        var pagedResult = movieMySqlRepository.getMovies(null, 0, 5);

        assertNotNull(pagedResult);
        assertEquals(5, pagedResult.getContent().size());
        assertEquals(0, pagedResult.getPageNumber());
        assertEquals(5, pagedResult.getPageSize());
        assertEquals(7, pagedResult.getTotalElements());
    }

    @Test
    @DisplayName("Should fetch paginated movies with name filter applied")
    void getMoviesWithNameFilter() {
        insertMovie("Inception");
        insertMovie("Interstellar");
        insertMovie("Dunkirk");

        var pagedResult = movieMySqlRepository.getMovies("Inception", 0, 10);

        assertNotNull(pagedResult);
        assertEquals(1, pagedResult.getContent().size());
        assertEquals(0, pagedResult.getPageNumber());
        assertEquals(10, pagedResult.getPageSize());
        assertEquals(1, pagedResult.getTotalElements());
    }

    @Test
    @DisplayName("Should return empty result when no movies match the name filter")
    void getMoviesWithNameFilter_NoMatch() {
        insertMovie("Inception");
        insertMovie("Interstellar");
        insertMovie("Dunkirk");

        var pagedResult = movieMySqlRepository.getMovies("Avatar", 0, 10);

        assertNotNull(pagedResult);
        assertEquals(0, pagedResult.getContent().size());
        assertEquals(0, pagedResult.getPageNumber());
        assertEquals(10, pagedResult.getPageSize());
        assertEquals(0, pagedResult.getTotalElements());
    }

    private void insertMovie(String name) {
        String movieInsertQuery = """ 
                INSERT INTO movie (name)
                VALUES ('%s');
                """;
        executeQuery(String.format(movieInsertQuery, name));
    }

}