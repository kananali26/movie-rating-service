package com.sky.movieratingservice.usecases.movie;

import com.sky.movieratingservice.usecases.repositories.MovieRepository;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateMovieUseCase {

    private final MovieRepository movieRepository;

    public void update(long movieId, int newCount, BigDecimal newAverage) {
        movieRepository.updateRatingCountAndAverage(movieId, newCount, newAverage);
    }
}
