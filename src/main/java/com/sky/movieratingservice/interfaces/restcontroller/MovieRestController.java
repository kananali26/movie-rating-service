package com.sky.movieratingservice.interfaces.restcontroller;

import com.sky.movieratingservice.domain.Movie;
import com.sky.movieratingservice.domain.PagedResult;
import com.sky.movieratingservice.openapi.interfaces.rest.MovieApi;
import com.sky.movieratingservice.openapi.interfaces.rest.dtos.MovieListResponseDto;
import com.sky.movieratingservice.usecases.movie.GetMovieUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MovieRestController implements MovieApi {

    private final GetMovieUseCase getMovieUseCase;
    private final PagedResultToMovieListResponseDtoConverter pagedResultConverter;

    @Override
    public ResponseEntity<MovieListResponseDto> getMovies(Integer pageNumber, Integer pageSize, String name) {
        log.info("Received request to get movies list with name: {}, page: {}, size: {}", name, pageNumber, pageSize);

        // Call the use case method with the name parameter and pagination parameters
        PagedResult<Movie> pagedResult = getMovieUseCase.getMovies(name, pageNumber, pageSize);

        // Map domain response to DTO using the converter
        MovieListResponseDto response = pagedResultConverter.convert(pagedResult);

        return ResponseEntity.ok(response);
    }
}
