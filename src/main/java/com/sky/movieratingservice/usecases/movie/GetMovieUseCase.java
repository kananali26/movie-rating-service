package com.sky.movieratingservice.usecases.movie;

import com.sky.movieratingservice.domain.Movie;
import com.sky.movieratingservice.domain.PagedResult;
import com.sky.movieratingservice.domain.exception.NotFoundException;
import com.sky.movieratingservice.usecases.repositories.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetMovieUseCase {

    private final MovieRepository movieRepository;

    public PagedResult<Movie> getMovies(String name, int pageNumber, int pageSize) {
        log.info("Getting movies with name filter: {}, page: {}, size: {}", name, pageNumber, pageSize);

        // Call repository to get movies and return the domain object directly
        return movieRepository.getMovies(name, pageNumber, pageSize);
    }

    public Movie getMovie(long movieId) {
        return movieRepository
                .getMovie(movieId)
                .orElseThrow(() -> new NotFoundException("Movie not found")); // Update exception as needed
    }
}