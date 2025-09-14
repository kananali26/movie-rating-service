package com.sky.movieratingservice.domain;

import lombok.Getter;

@Getter
public class Rating {
    private final User user;
    private final Movie movie;
    private final Integer value;

    public Rating(User user, Movie movie, Integer value) {
        if (value == null || value < 1 || value > 10) {
            throw new IllegalArgumentException("Rating value must be between 1 and 10");
        }
        this.user = user;
        this.movie = movie;
        this.value = value;
    }

}
