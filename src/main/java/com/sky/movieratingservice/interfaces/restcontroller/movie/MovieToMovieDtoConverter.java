package com.sky.movieratingservice.interfaces.restcontroller.movie;

import com.sky.movieratingservice.domain.Movie;
import com.sky.movieratingservice.openapi.interfaces.rest.dtos.MovieDto;
import org.springframework.stereotype.Component;

@Component
class MovieToMovieDtoConverter {

    public MovieDto convert(Movie movie) {
        return MovieDto.builder()
                .id((int) movie.getId())
                .name(movie.getName())
                .build();
    }
}
