package com.sky.movieratingservice.usecases.rating;

import com.sky.movieratingservice.domain.Movie;
import com.sky.movieratingservice.usecases.movie.GetMovieUseCase;
import com.sky.movieratingservice.usecases.movie.UpsertMovieUseCase;
import com.sky.movieratingservice.usecases.repositories.RatingRepository;
import jakarta.persistence.OptimisticLockException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteMovieRatingUseCase {

    private final RatingRepository ratingRepository;
    private final GetMovieUseCase getMovieUseCase;
    private final UpsertMovieUseCase upsertMovieUseCase;

    @Transactional
    @Retryable(retryFor = {OptimisticLockException.class, ObjectOptimisticLockingFailureException.class})
    public void deleteMovieRating(long movieId, String email) {
        Movie movie = getMovieUseCase.getMovie(movieId);

        Optional<Integer> optionalInteger = ratingRepository.getRatingValue(movieId, email);

        if (optionalInteger.isEmpty()) {
            return;
        }

        ratingRepository.deleteRating(movieId, email);

        movie = movie.removeRating(optionalInteger.get());

        upsertMovieUseCase.update(movieId, movie.ratingCount(), movie.averageRating());
    }
}
