package com.sky.movieratingservice.interfaces.repositories.rating;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

interface RatingJpaRepository extends JpaRepository<RatingDbo,Long> {
    
    @Query("SELECT r FROM RatingDbo r WHERE r.movie.id = :movieId AND r.user.email = :email")
    Optional<RatingDbo> findByMovieIdAndUserEmail(long movieId, String email);

    @Modifying
    @Query("UPDATE RatingDbo r SET r.rating = :rating WHERE r.movie.id = :movieId AND r.user.id = :userId")
    void updateRatingByMovieIdAndUserId(long movieId, long userId, Integer rating);

    @Query("SELECT r.rating FROM RatingDbo r WHERE r.movie.id = :movieId AND r.user.email = :email")
    Optional<Integer> findRatingByMovieIdAndUserId(long movieId, String email);

}
