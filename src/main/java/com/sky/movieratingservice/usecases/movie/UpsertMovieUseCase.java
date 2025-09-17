package com.sky.movieratingservice.usecases.movie;

import com.sky.movieratingservice.domain.Movie;
import com.sky.movieratingservice.usecases.repositories.MovieRepository;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpsertMovieUseCase {

    private final MovieRepository movieRepository;

    public void create(String name) {
        Movie movie = Movie.builder().name(name).ratingCount(0).averageRating(BigDecimal.ZERO).build();
        movieRepository.createMovie(movie);
    }

    public void update(long movieId, int newCount, BigDecimal newAverage) {
        log.info("Updating movie aggregates [id={}, newRatingCount={}, newAverageRating={}]",
                movieId, newCount, newAverage);
        movieRepository.updateRatingCountAndAverage(movieId, newCount, newAverage);
    }
}
