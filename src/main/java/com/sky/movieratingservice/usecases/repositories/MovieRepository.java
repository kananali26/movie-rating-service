package com.sky.movieratingservice.usecases.repositories;

import com.sky.movieratingservice.domain.Movie;
import com.sky.movieratingservice.domain.PagedResult;

public interface MovieRepository {

    PagedResult<Movie> getMovies(String name, int pageNumber, int pageSize);

    Movie createMovie(String name);
}
