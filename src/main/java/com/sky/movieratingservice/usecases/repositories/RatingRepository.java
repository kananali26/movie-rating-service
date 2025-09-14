package com.sky.movieratingservice.usecases.repositories;

import com.sky.movieratingservice.domain.Rating;
import java.util.Optional;

public interface RatingRepository {

    void create(Rating rating);

    void deleteRating(long movieId, String email);

    boolean existsByMovieIdAndUserId(Long movieId, Long userId);

    void update(Rating rating);

    Optional<Integer> getRatingValue(long movieId, String email);

}
