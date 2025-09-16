package com.sky.movieratingservice.interfaces.repositories.movie;

import static org.junit.jupiter.api.Assertions.*;

import com.sky.movieratingservice.domain.Movie;
import com.sky.movieratingservice.utils.BaseIntegrationTest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MovieMySqlRepositoryIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private MovieMySqlRepository movieMySqlRepository;

    @BeforeEach
    void cleanupMovies() {
        String deleteQuery = "DELETE FROM movies;";
        executeQuery(deleteQuery);
    }

    @Test
    @DisplayName("Should fetch paginated movies when no filter is applied")
    void testGetMoviesWithoutFilter() {
        insertMovie("The Shawshank Redemption", 1, 9.3);
        insertMovie("The Godfather", 1, 9.2);
        insertMovie("The Dark Knight", 1, 9.0);
        insertMovie("Pulp Fiction", 2, 8.9);
        insertMovie("Fight Club", 3, 8.8);
        insertMovie("Goodfellas", 2, 8.7);
        insertMovie("The Green Mile", 1, 8.6);

        var pagedResult = movieMySqlRepository.getMovies(null, 0, 5);

        assertNotNull(pagedResult);
        assertEquals(5, pagedResult.content().size());
        assertEquals(0, pagedResult.pageNumber());
        assertEquals(5, pagedResult.pageSize());
        assertEquals(7, pagedResult.totalElements());
    }

    @Test
    @DisplayName("Should fetch paginated movies with name filter applied")
    void getMoviesWithNameFilter() {
        insertMovie("Inception", 1, 8.8);
        insertMovie("Interstellar", 2, 8.6);
        insertMovie("Dunkirk", 1, 7.9);

        var pagedResult = movieMySqlRepository.getMovies("Inception", 0, 10);

        assertNotNull(pagedResult);
        assertEquals(1, pagedResult.content().size());
        assertEquals(0, pagedResult.pageNumber());
        assertEquals(10, pagedResult.pageSize());
        assertEquals(1, pagedResult.totalElements());
    }

    @Test
    @DisplayName("Should return empty result when no movies match the name filter")
    void getMoviesWithNameFilter_NoMatch() {
        insertMovie("Inception", 1, 8.8);
        insertMovie("Interstellar", 2, 8.6);
        insertMovie("Dunkirk", 1, 7.9);

        var pagedResult = movieMySqlRepository.getMovies("Avatar", 0, 10);

        assertNotNull(pagedResult);
        assertEquals(0, pagedResult.content().size());
        assertEquals(0, pagedResult.pageNumber());
        assertEquals(10, pagedResult.pageSize());
        assertEquals(0, pagedResult.totalElements());
    }

    @Test
    @DisplayName("Should fetch top rated movies by average rating")
    void testGetTopRatedMovies() {
        insertMovie("The Shawshank Redemption", 2, 10);
        insertMovie("Pulp Fiction", 1, 8.0);
        insertMovie("The Godfather", 3, 9.5);

        List<Movie> movies = movieMySqlRepository.getTopRatedMovies(10);

        assertNotNull(movies);
        assertEquals(3, movies.size());
        assertEquals("The Shawshank Redemption", movies.getFirst().name());
        assertEquals(BigDecimal.valueOf(10).setScale(2, RoundingMode.HALF_UP), movies.getFirst().averageRating().setScale(2, RoundingMode.HALF_UP));
        assertEquals(2, movies.getFirst().ratingCount());

        assertEquals("The Godfather", movies.get(1).name());
        assertEquals(BigDecimal.valueOf(9.5).setScale(2, RoundingMode.HALF_UP), movies.get(1).averageRating().setScale(2, RoundingMode.HALF_UP));
        assertEquals(3, movies.get(1).ratingCount());

        assertEquals("Pulp Fiction", movies.get(2).name());
        assertEquals(BigDecimal.valueOf(8).setScale(2, RoundingMode.HALF_UP), movies.get(2).averageRating().setScale(2, RoundingMode.HALF_UP));
        assertEquals(1, movies.get(2).ratingCount());
    }

    private void insertMovie(String name, int ratingCount, double averageRating) {
        String movieInsertQuery = """ 
                INSERT INTO movies (name, rating_count, average_rating)
                VALUES ('%s', %d, %.2f);
                """;
        executeQuery(String.format(movieInsertQuery, name, ratingCount, averageRating));
    }
}