package com.sky.movieratingservice.usecases.movie;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.sky.movieratingservice.domain.Movie;
import com.sky.movieratingservice.domain.PaginatedResult;
import com.sky.movieratingservice.domain.exception.NotFoundException;
import com.sky.movieratingservice.usecases.repositories.MovieRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetMovieUseCaseTest {

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private GetMovieUseCase getMovieUseCase;

    @Test
    @DisplayName("Should return paginated list of movies when getMovies is called")
    void testGetMovies() {
        String name = "test";
        int pageNumber = 0;
        int pageSize = 10;

        List<Movie> movies = List.of(new Movie(1L, "Test Movie", 10, BigDecimal.valueOf(8.5)));
        PaginatedResult<Movie> expected = new PaginatedResult<>(movies, pageNumber, pageSize, 1);

        when(movieRepository.getMovies(name, pageNumber, pageSize)).thenReturn(expected);

        PaginatedResult<Movie> result = getMovieUseCase.getMovies(name, pageNumber, pageSize);

        assertEquals(expected, result);
        verify(movieRepository).getMovies(name, pageNumber, pageSize);
    }

    @Test
    @DisplayName("Should return list of top rated movies when getTopRatedMovies is called")
    void testGetTopRatedMovies() {
        int limit = 10;

        List<Movie> movies = List.of(new Movie(1L, "Top Rated Movie", 100, BigDecimal.valueOf(9.5)));

        when(movieRepository.getTopRatedMovies(limit)).thenReturn(movies);

        List<Movie> result = getMovieUseCase.getTopRatedMovies(limit);

        assertEquals(movies, result);
        verify(movieRepository).getTopRatedMovies(limit);
    }

    @Test
    @DisplayName("getMovie() should return the movie when it exists")
    void getMovie_returnsMovie_whenFound() {
        long movieId = 42L;
        Movie movie = new Movie(movieId, "Found Movie", 50, new BigDecimal("8.2"));
        when(movieRepository.getMovie(movieId)).thenReturn(Optional.of(movie));

        Movie result = getMovieUseCase.getMovie(movieId);

        assertSame(movie, result);
        verify(movieRepository, times(1)).getMovie(movieId);
        verifyNoMoreInteractions(movieRepository);
    }

    @Test
    @DisplayName("getMovie() should throw NotFoundException with id in message when movie is missing")
    void getMovie_throwsNotFound_whenMissing() {
        long movieId = 99L;
        when(movieRepository.getMovie(movieId)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> getMovieUseCase.getMovie(movieId)
        );

        assertEquals(String.format("Movie with id:%d not found", movieId), ex.getMessage());
        verify(movieRepository, times(1)).getMovie(movieId);
        verifyNoMoreInteractions(movieRepository);
    }
}
