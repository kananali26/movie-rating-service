package com.sky.movieratingservice.interfaces.repositories.movie;

import com.sky.movieratingservice.domain.Movie;
import org.springframework.stereotype.Component;

@Component
class MovieDboToDomainMovieConverter {

    public Movie convert(MovieDbo movieDbo) {
        return new Movie(movieDbo.getId(), movieDbo.getName());
    }
}
