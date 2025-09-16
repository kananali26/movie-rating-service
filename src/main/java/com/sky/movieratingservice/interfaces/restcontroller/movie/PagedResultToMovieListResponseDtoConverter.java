package com.sky.movieratingservice.interfaces.restcontroller.movie;

import com.sky.movieratingservice.domain.Movie;
import com.sky.movieratingservice.domain.PaginatedResult;
import com.sky.movieratingservice.openapi.interfaces.rest.dtos.MovieListResponseDto;
import com.sky.movieratingservice.openapi.interfaces.rest.dtos.PaginationInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class PagedResultToMovieListResponseDtoConverter {

    private final MovieToMovieDtoConverter movieToMovieDtoConverter;

    public MovieListResponseDto convert(PaginatedResult<Movie> paginatedResult) {
        PaginationInfoDto paginationDto = new PaginationInfoDto()
                .pageNumber(paginatedResult.pageNumber())
                .pageSize(paginatedResult.pageSize())
                .totalRecords((int) paginatedResult.totalElements());

        return MovieListResponseDto.builder()
                .paginationInfo(paginationDto)
                .movies(paginatedResult.content().stream()
                        .map(movieToMovieDtoConverter::convert)
                        .toList())
                .build();
    }
}