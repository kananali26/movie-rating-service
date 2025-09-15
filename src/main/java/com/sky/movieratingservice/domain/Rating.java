package com.sky.movieratingservice.domain;

import com.sky.movieratingservice.domain.exception.InvalidRequestException;

public record Rating(User user, Movie movie, Integer value) {

    public Rating {
        if (value == null || value < 1 || value > 10) {
            throw new InvalidRequestException("Rating value must be between 1 and 10");
        }
    }
}
