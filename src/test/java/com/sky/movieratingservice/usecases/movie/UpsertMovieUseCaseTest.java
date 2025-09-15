package com.sky.movieratingservice.usecases.movie;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.sky.movieratingservice.usecases.repositories.MovieRepository;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UpsertMovieUseCaseTest {

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private UpsertMovieUseCase upsertMovieUseCase;

    @Test
    @DisplayName("Should create movie by delegating to repository")
    void create_delegatesToRepository() {
        String name = "Inception";

        upsertMovieUseCase.create(name);

        verify(movieRepository, times(1)).createMovie(name);
        verifyNoMoreInteractions(movieRepository);
    }

    @Test
    @DisplayName("update(id,count,avg) should delegate to repository.updateRatingCountAndAverage with same args")
    void update_delegatesToRepository() {
        long movieId = 42L;
        int newCount = 7;
        BigDecimal newAverage = new BigDecimal("9.3");

        upsertMovieUseCase.update(movieId, newCount, newAverage);

        verify(movieRepository, times(1))
                .updateRatingCountAndAverage(movieId, newCount, newAverage);
        verifyNoMoreInteractions(movieRepository);
    }

}