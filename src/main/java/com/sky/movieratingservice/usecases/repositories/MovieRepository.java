package com.sky.movieratingservice.usecases.repositories;

import com.sky.movieratingservice.domain.Movie;
import com.sky.movieratingservice.domain.PagedResult;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface MovieRepository {

    PagedResult<Movie> getMovies(String name, int pageNumber, int pageSize);

    Optional<Movie> getMovie(long movieId);

    void updateRatingCountAndAverage(long movieId, int newCount, BigDecimal newAverage);

    List<Movie> getTopRatedMovies(int topN);
    
}
