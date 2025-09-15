package com.sky.movieratingservice.usecases.movie;

import com.sky.movieratingservice.domain.Movie;
import com.sky.movieratingservice.domain.PaginatedResult;
import com.sky.movieratingservice.domain.exception.NotFoundException;
import com.sky.movieratingservice.usecases.repositories.MovieRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetMovieUseCase {

    private final MovieRepository movieRepository;

    public PaginatedResult<Movie> getMovies(String name, int pageNumber, int pageSize) {
        return movieRepository.getMovies(name, pageNumber, pageSize);
    }

    public List<Movie> getTopRatedMovies(int limit) {
        return movieRepository.getTopRatedMovies(limit);
    }

    public Movie getMovie(long movieId) {
        return movieRepository
                .getMovie(movieId)
                .orElseThrow(() -> new NotFoundException(String.format("Movie with id:%d not found", movieId)));
    }
}