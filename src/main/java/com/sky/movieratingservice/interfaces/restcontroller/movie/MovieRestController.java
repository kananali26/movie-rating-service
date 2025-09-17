package com.sky.movieratingservice.interfaces.restcontroller.movie;

import com.sky.movieratingservice.domain.Movie;
import com.sky.movieratingservice.domain.PaginatedResult;
import com.sky.movieratingservice.openapi.interfaces.rest.MovieApi;
import com.sky.movieratingservice.openapi.interfaces.rest.dtos.CreateMovieRequestDto;
import com.sky.movieratingservice.openapi.interfaces.rest.dtos.MovieDto;
import com.sky.movieratingservice.openapi.interfaces.rest.dtos.MovieListResponseDto;
import com.sky.movieratingservice.openapi.interfaces.rest.dtos.RateMovieRequestDto;
import com.sky.movieratingservice.usecases.movie.GetMovieUseCase;
import com.sky.movieratingservice.usecases.movie.UpsertMovieUseCase;
import com.sky.movieratingservice.usecases.rating.DeleteMovieRatingUseCase;
import com.sky.movieratingservice.usecases.rating.RateMovieUseCase;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MovieRestController implements MovieApi {

    private final GetMovieUseCase getMovieUseCase;
    private final RateMovieUseCase rateMovieUseCase;
    private final UpsertMovieUseCase upsertMovieUseCase;
    private final DeleteMovieRatingUseCase deleteMovieRatingUseCase;
    private final MovieToMovieDtoConverter movieToMovieDtoConverter;
    private final PagedResultToMovieListResponseDtoConverter pagedResultConverter;

    @Override
    public ResponseEntity<Void> createMovie(CreateMovieRequestDto createMovieRequestDto) {
        upsertMovieUseCase.create(createMovieRequestDto.getName());
        return ResponseEntity.status(201).build();
    }

    @Override
    public ResponseEntity<MovieListResponseDto> getMovies(Integer pageNumber, Integer pageSize, String name) {
        log.info("Getting movies list with name: {}, page: {}, size: {}", name, pageNumber, pageSize);

        PaginatedResult<Movie> paginatedResult = getMovieUseCase.getMovies(name, pageNumber, pageSize);

        MovieListResponseDto response = pagedResultConverter.convert(paginatedResult);

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<MovieListResponseDto> getTopRatedMovies(Integer limit) {
        log.info("Getting top rated movies with limit: {}", limit);

        List<Movie> topRatedMovies = getMovieUseCase.getTopRatedMovies(limit);

        List<MovieDto> movieDtos = topRatedMovies
                .stream()
                .map(movieToMovieDtoConverter::convert)
                .toList();

        return ResponseEntity.ok(MovieListResponseDto.builder().movies(movieDtos).build());
    }

    @Override
    public ResponseEntity<Void> rateMovie(Integer movieId, RateMovieRequestDto rateMovieRequestDto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Rating movie with id: {}, username: {}", movieId, username);

        rateMovieUseCase.rateMovie(username, movieId, rateMovieRequestDto.getValue());
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> deleteMovieRating(Integer movieId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Deleting movie rating with id: {}", movieId);
        deleteMovieRatingUseCase.deleteMovieRating(movieId, username);
        return ResponseEntity.ok().build();
    }
}
