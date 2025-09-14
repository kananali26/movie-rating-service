package com.sky.movieratingservice.interfaces.repositories.movie;

import com.sky.movieratingservice.domain.Movie;
import com.sky.movieratingservice.domain.PagedResult;
import com.sky.movieratingservice.usecases.repositories.MovieRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class MovieMySqlRepository implements MovieRepository {

    private final MovieJpaRepository movieJpaRepository;
    private final MovieDboToDomainMovieConverter movieConverter;

    @Override
    public PagedResult<Movie> getMovies(String name, int pageNumber, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);

        // Get the page of MovieDbo objects from the JPA repository
        Page<MovieDbo> movieDbosPage = movieJpaRepository.findAll(
            MovieJpaRepository.Specifications.filterByName(name), pageRequest);

        List<Movie> movies = movieDbosPage.getContent().stream()
            .map(movieConverter::convert)
            .toList();

        return new PagedResult<>(
            movies,
            pageNumber,
            pageSize,
            movieDbosPage.getTotalElements()
        );
    }

    @Override
    public Optional<Movie> getMovie(long movieId) {
        return movieJpaRepository
                .findById(movieId)
                .map(movieConverter::convert);
    }

    @Override
    public void updateRatingCountAndAverage(long movieId, int newCount, BigDecimal newAverage) {
        movieJpaRepository.updateRatingCountAndAverageById(movieId, newCount, newAverage);
    }


}
