package com.sky.movieratingservice.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MovieTest {

    @Test
    @DisplayName("should correctly calculate new average when adding a rating")
    void shouldCorrectlyAddRating() {
        Movie movie = Movie.builder()
                .id(1L)
                .name("The Matrix")
                .ratingCount(1)
                .averageRating(new BigDecimal("5.00"))
                .build();

        Movie updated = movie.addRating(8);

        assertEquals(2, updated.ratingCount());
        assertEquals(new BigDecimal("6.50"), updated.averageRating());
    }

    @Test
    @DisplayName("should correctly update average when a rating is changed")
    void shouldCorrectlyUpdateRating() {
        Movie movie = Movie.builder()
                .id(2L)
                .name("Interstellar")
                .ratingCount(2)
                .averageRating(new BigDecimal("8.50"))
                .build();

        Movie updated = movie.updateRating(7, 9);

        assertEquals(new BigDecimal("9.50"), updated.averageRating());
    }

    @Test
    @DisplayName("should correctly recalculate average when removing a rating")
    void shouldCorrectlyRemoveRating() {
        Movie movie = Movie.builder()
                .id(3L)
                .name("The Dark Knight")
                .ratingCount(3)
                .averageRating(new BigDecimal("9.00"))
                .build();

        Movie updated = movie.removeRating(10);

        assertEquals(2, updated.ratingCount());
        assertEquals(new BigDecimal("8.50"), updated.averageRating());
    }

}
