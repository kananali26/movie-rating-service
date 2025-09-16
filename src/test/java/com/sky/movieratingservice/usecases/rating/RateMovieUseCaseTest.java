package com.sky.movieratingservice.usecases.rating;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sky.movieratingservice.domain.Movie;
import com.sky.movieratingservice.domain.Rating;
import com.sky.movieratingservice.domain.User;
import com.sky.movieratingservice.usecases.movie.GetMovieUseCase;
import com.sky.movieratingservice.usecases.movie.UpsertMovieUseCase;
import com.sky.movieratingservice.usecases.repositories.RatingRepository;
import com.sky.movieratingservice.usecases.user.GetUserUseCase;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RateMovieUseCaseTest {

    @Mock
    private GetUserUseCase getUserUseCase;
    @Mock
    private GetMovieUseCase getMovieUseCase;
    @Mock
    private UpsertMovieUseCase upsertMovieUseCase;
    @Mock
    private RatingRepository ratingRepository;
    @Mock
    private User user;

    @Mock
    private Movie movie;

    @Mock
    private Movie updatedMovie;

    @InjectMocks
    private RateMovieUseCase rateMovieUseCase;

    @Captor
    private ArgumentCaptor<Integer> countCaptor;
    @Captor
    private ArgumentCaptor<BigDecimal> avgCaptor;

    @Test
    @DisplayName("rateMovie: creates rating when not existing, then updates movie aggregates")
    void rateMovie_creates_whenNotExisting() {
        String email = "john@example.com";
        long movieId = 42L;
        int newValue = 9;

        when(user.email()).thenReturn(email);

        when(updatedMovie.ratingCount()).thenReturn(11);
        when(updatedMovie.averageRating()).thenReturn(new BigDecimal("8.90"));

        when(getUserUseCase.getUser(email)).thenReturn(user);
        when(getMovieUseCase.getMovie(movieId)).thenReturn(movie);
        when(ratingRepository.getRatingValue(movieId, email)).thenReturn(Optional.empty());
        when(movie.addRating(newValue)).thenReturn(updatedMovie);

        rateMovieUseCase.rateMovie(email, movieId, newValue);

        verify(getUserUseCase).getUser(email);
        verify(getMovieUseCase).getMovie(movieId);
        verify(ratingRepository).getRatingValue(movieId, email);

        verify(ratingRepository).create(any(Rating.class));
        verify(ratingRepository, never()).update(any(Rating.class));
        verify(movie).addRating(newValue);

        verify(upsertMovieUseCase).update(eq(movieId), countCaptor.capture(), avgCaptor.capture());
        assertEquals(11, countCaptor.getValue());
        assertEquals(0, avgCaptor.getValue().compareTo(new BigDecimal("8.90")));
    }

    @Test
    @DisplayName("rateMovie: updates rating when existing, then updates movie aggregates using old vs new value")
    void rateMovie_updates_whenExisting() {
        String email = "john@example.com";
        long movieId = 42L;
        int oldValue = 7;
        int newValue = 9;

        when(user.email()).thenReturn(email);

        when(updatedMovie.ratingCount()).thenReturn(10); // count unchanged for update
        when(updatedMovie.averageRating()).thenReturn(new BigDecimal("8.50"));

        when(getUserUseCase.getUser(email)).thenReturn(user);
        when(getMovieUseCase.getMovie(movieId)).thenReturn(movie);
        when(ratingRepository.getRatingValue(movieId, email)).thenReturn(Optional.of(oldValue));
        when(movie.updateRating(oldValue, newValue)).thenReturn(updatedMovie);

        rateMovieUseCase.rateMovie(email, movieId, newValue);

        verify(getUserUseCase).getUser(email);
        verify(getMovieUseCase).getMovie(movieId);
        verify(ratingRepository).getRatingValue(movieId, email);

        verify(ratingRepository).update(any(Rating.class));
        verify(ratingRepository, never()).create(any(Rating.class));
        verify(movie).updateRating(oldValue, newValue);
        verify(upsertMovieUseCase).update(eq(movieId), countCaptor.capture(), avgCaptor.capture());
        assertEquals(10, countCaptor.getValue());
        assertEquals(0, avgCaptor.getValue().compareTo(new BigDecimal("8.50")));
    }

}
