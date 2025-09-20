package com.sky.movieratingservice.interfaces.repositories.movie;

import com.sky.movieratingservice.domain.Movie;
import com.sky.movieratingservice.domain.PaginatedResult;
import com.sky.movieratingservice.domain.exception.NotFoundException;
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
    private final MovieDboToMovieConverter movieConverter;

    @Override
    public void createMovie(Movie movie) {
        MovieDbo movieDbo = new MovieDbo();
        movieDbo.setName(movie.name());
        movieDbo.setRatingCount(movie.ratingCount());
        movieDbo.setAverageRating(movie.averageRating());

        movieJpaRepository.save(movieDbo);
    }

    @Override
    public PaginatedResult<Movie> getMovies(String name, int pageNumber, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);

        Page<MovieDbo> movieDbosPage = movieJpaRepository.findAll(
            MovieJpaRepository.Specifications.filterByName(name), pageRequest);

        List<Movie> movies = movieDbosPage.getContent().stream()
            .map(movieConverter::convert)
            .toList();

        return new PaginatedResult<>(movies, pageNumber, pageSize, movieDbosPage.getTotalElements());
    }

    @Override
    public Optional<Movie> getMovie(long movieId) {
        return movieJpaRepository
                .findById(movieId)
                .map(movieConverter::convert);
    }

    @Override
    public void updateRatingCountAndAverage(long movieId, int newCount, BigDecimal newAverage) {
        MovieDbo movieDbo = movieJpaRepository
                .findById(movieId)
                .orElseThrow(() -> new NotFoundException(String.format("Movie not found: id: %d", movieId)));

        movieDbo.setRatingCount(newCount);
        movieDbo.setAverageRating(newAverage);

        movieJpaRepository.save(movieDbo);
    }

    @Override
    public List<Movie> getTopRatedMovies(int topN) {
        PageRequest pageRequest = PageRequest.of(0, topN);
        List<MovieDbo> movieDboList = movieJpaRepository.findTopRatedMovies(pageRequest);

        return movieDboList.stream()
                .map(movieConverter::convert)
                .toList();
    }

}
