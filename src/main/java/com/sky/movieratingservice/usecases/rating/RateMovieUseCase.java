package com.sky.movieratingservice.usecases.rating;

import com.sky.movieratingservice.domain.Movie;
import com.sky.movieratingservice.domain.Rating;
import com.sky.movieratingservice.domain.User;
import com.sky.movieratingservice.usecases.movie.GetMovieUseCase;
import com.sky.movieratingservice.usecases.movie.UpdateMovieUseCase;
import com.sky.movieratingservice.usecases.repositories.RatingRepository;
import com.sky.movieratingservice.usecases.user.GetUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RateMovieUseCase {

    private final GetUserUseCase getUserUseCase;
    private final GetMovieUseCase getMovieUseCase;
    private final UpdateMovieUseCase updateMovieUseCase;
    private final RatingRepository ratingRepository;

    @Transactional
    public void rateMovie(String email, long movieId, int value) {
        User user = getUserUseCase.getUser(email);
        Movie movie = getMovieUseCase.getMovie(movieId);

        Rating rating = new Rating(user, movie, value);

        boolean isExists = ratingRepository.existsByMovieIdAndUserId(movieId, user.getId());

        if (isExists) {
            int oldValue = ratingRepository.getRatingValue(movieId, user.getEmail()).get();

            ratingRepository.update(rating);
            movie = movie.updateRating(oldValue, value);
        } else {
            ratingRepository.create(rating);
            movie = movie.addRating(value);
        }

        updateMovieUseCase.update(movieId, movie.ratingCount(), movie.averageRating());
    }
}
