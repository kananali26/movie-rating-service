package com.sky.movieratingservice.interfaces.restcontroller.movie;

import com.sky.movieratingservice.domain.Movie;
import com.sky.movieratingservice.domain.PagedResult;
import com.sky.movieratingservice.openapi.interfaces.rest.MovieApi;
import com.sky.movieratingservice.openapi.interfaces.rest.dtos.MovieListResponseDto;
import com.sky.movieratingservice.openapi.interfaces.rest.dtos.RateMovieRequestDto;
import com.sky.movieratingservice.usecases.movie.GetMovieUseCase;
import com.sky.movieratingservice.usecases.rating.DeleteMovieRatingUseCase;
import com.sky.movieratingservice.usecases.rating.RateMovieUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MovieRestController implements MovieApi {

    private final GetMovieUseCase getMovieUseCase;
    private final RateMovieUseCase rateMovieUseCase;
    private final DeleteMovieRatingUseCase deleteMovieRatingUseCase;
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

    @Override
    public ResponseEntity<Void> rateMovie(Integer movieId, RateMovieRequestDto rateMovieRequestDto) {
        // we should get email from security context in real application
        rateMovieUseCase.rateMovie("john.doe@example.com", movieId, rateMovieRequestDto.getValue());
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> deleteMovieRating(Integer movieId) {
        deleteMovieRatingUseCase.deleteMovieRating(movieId, "john.doe@example.com");
        return ResponseEntity.ok().build();
    }
}
