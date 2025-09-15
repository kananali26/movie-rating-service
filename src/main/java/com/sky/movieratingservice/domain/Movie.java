package com.sky.movieratingservice.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.Builder;

@Builder
public record Movie(long id, String name, Integer ratingCount, BigDecimal averageRating) {

    public Movie addRating(int value) {
        BigDecimal newAverage = (averageRating.multiply(BigDecimal.valueOf(ratingCount))
                .add(BigDecimal.valueOf(value)))
                .divide(BigDecimal.valueOf((long)ratingCount + 1), 2, RoundingMode.HALF_UP);

        return Movie.builder()
                .id(this.id)
                .name(this.name)
                .averageRating(newAverage)
                .ratingCount(this.ratingCount + 1)
                .build();
    }

    public Movie updateRating(int oldValue, int newValue) {
        BigDecimal newAverage = averageRating.add(
                BigDecimal.valueOf((long)newValue - oldValue)
                        .divide(BigDecimal.valueOf(ratingCount), 2, RoundingMode.HALF_UP)
        );

        return Movie.builder()
                .id(this.id)
                .name(this.name)
                .averageRating(newAverage)
                .ratingCount(this.ratingCount)
                .build();
    }

    public Movie removeRating(int value) {
        if (ratingCount > 1) {
            BigDecimal newAverage = (averageRating.multiply(BigDecimal.valueOf(ratingCount))
                    .subtract(BigDecimal.valueOf(value)))
                    .divide(BigDecimal.valueOf((long)ratingCount - 1), 2, RoundingMode.HALF_UP);

            return Movie.builder()
                    .id(this.id)
                    .name(this.name)
                    .averageRating(newAverage)
                    .ratingCount(this.ratingCount - 1)
                    .build();
        } else {
            return Movie.builder()
                    .id(this.id)
                    .name(this.name)
                    .averageRating(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP))
                    .ratingCount(0)
                    .build();
        }
    }
}
