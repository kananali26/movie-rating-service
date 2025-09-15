package com.sky.movieratingservice.interfaces.repositories.movie;

import com.sky.movieratingservice.domain.Movie;
import com.sky.movieratingservice.domain.PaginatedResult;
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
    public void createMovie(String name) {
        MovieDbo movieDbo = new MovieDbo();
        movieDbo.setName(name);
        movieDbo.setRatingCount(0);
        movieDbo.setAverageRating(BigDecimal.ZERO);
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
        movieJpaRepository.updateRatingCountAndAverageById(movieId, newCount, newAverage);
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
