package com.sky.movieratingservice.interfaces.repositories.rating;

import com.sky.movieratingservice.domain.Rating;
import com.sky.movieratingservice.interfaces.repositories.movie.MovieDbo;
import com.sky.movieratingservice.interfaces.repositories.user.UserDbo;
import com.sky.movieratingservice.usecases.repositories.RatingRepository;
import jakarta.persistence.EntityManager;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class RatingMySqlRepository implements RatingRepository {

    private final EntityManager entityManager;
    private final RatingJpaRepository ratingJpaRepository;

    @Override
    public void create(Rating rating) {
        UserDbo userDbo = entityManager.getReference(UserDbo.class, rating.user().id());
        MovieDbo movieDbo = entityManager.getReference(MovieDbo.class, rating.movie().id());

        RatingDbo ratingDbo = new RatingDbo();
        ratingDbo.setUser(userDbo);
        ratingDbo.setMovie(movieDbo);
        ratingDbo.setRating(rating.value());

        ratingJpaRepository.save(ratingDbo);
    }

    @Override
    public void deleteRating(long movieId, String email) {
        ratingJpaRepository
                .findByMovieIdAndUserEmail(movieId, email)
                .ifPresent(ratingJpaRepository::delete);
    }

    @Override
    public void update(Rating rating) {
        ratingJpaRepository.updateRatingByMovieIdAndUserId(rating.movie().id(), rating.user().id(), rating.value());
    }

    @Override
    public Optional<Integer> getRatingValue(long movieId, String email) {
        return ratingJpaRepository.findRatingByMovieIdAndUserId(movieId, email);
    }

}
