package com.sky.movieratingservice.usecases.rating;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sky.movieratingservice.domain.Movie;
import com.sky.movieratingservice.usecases.movie.GetMovieUseCase;
import com.sky.movieratingservice.usecases.movie.UpsertMovieUseCase;
import com.sky.movieratingservice.usecases.repositories.RatingRepository;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DeleteMovieRatingUseCaseTest {

    @Mock
    private RatingRepository ratingRepository;
    @Mock
    private GetMovieUseCase getMovieUseCase;
    @Mock
    private UpsertMovieUseCase upsertMovieUseCase;

    @InjectMocks
    private DeleteMovieRatingUseCase deleteMovieRatingUseCase;

    @Test
    @DisplayName("should do nothing if rating does not exist")
    void shouldDoNothingIfRatingNotExist() {
        long movieId = 1L;
        String email = "user@example.com";

        Movie movie = Movie.builder()
                .id(movieId)
                .name("Inception")
                .ratingCount(2)
                .averageRating(new BigDecimal("9.00"))
                .build();

        when(getMovieUseCase.getMovie(movieId)).thenReturn(movie);
        when(ratingRepository.getRatingValue(movieId, email)).thenReturn(Optional.empty());

        deleteMovieRatingUseCase.deleteMovieRating(movieId, email);

        verify(ratingRepository, never()).deleteRating(anyLong(), anyString());
        verify(upsertMovieUseCase, never()).update(anyLong(), anyInt(), any());
    }


    @Test
    @DisplayName("should delete rating and update movie correctly")
    void shouldDeleteRatingAndUpdateMovie() {
        long movieId = 1L;
        String email = "user@example.com";
        int oldRating = 8;

        Movie movie = Movie.builder()
                .id(movieId)
                .name("Inception")
                .ratingCount(2)
                .averageRating(new BigDecimal("9.00"))
                .build();

        when(getMovieUseCase.getMovie(movieId)).thenReturn(movie);
        when(ratingRepository.getRatingValue(movieId, email)).thenReturn(Optional.of(oldRating));

        deleteMovieRatingUseCase.deleteMovieRating(movieId, email);

        verify(ratingRepository, times(1)).deleteRating(movieId, email);

        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Integer> countCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<BigDecimal> avgCaptor = ArgumentCaptor.forClass(BigDecimal.class);

        verify(upsertMovieUseCase, times(1))
                .update(idCaptor.capture(), countCaptor.capture(), avgCaptor.capture());

        assertEquals(movieId, idCaptor.getValue());
        assertEquals(1, countCaptor.getValue());
        assertEquals(new BigDecimal("10.00"), avgCaptor.getValue());
    }

}