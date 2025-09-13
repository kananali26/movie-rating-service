package com.sky.movieratingservice.interfaces.repositories.movie;

import com.sky.movieratingservice.domain.Movie;
import com.sky.movieratingservice.domain.PagedResult;
import com.sky.movieratingservice.usecases.repositories.MovieRepository;
import java.util.List;
import java.util.stream.Collectors;
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
            .collect(Collectors.toList());

        return new PagedResult<>(
            movies,
            pageNumber,
            pageSize,
            movieDbosPage.getTotalElements()
        );
    }

    @Override
    public Movie createMovie(String name) {
        // Create a new MovieDbo object
        MovieDbo movieDbo = new MovieDbo();
        movieDbo.setName(name);

        // Save the MovieDbo object to the database
        MovieDbo savedMovieDbo = movieJpaRepository.save(movieDbo);

        // Convert the saved MovieDbo to a domain Movie object and return it
        return movieConverter.convert(savedMovieDbo);
    }

}
