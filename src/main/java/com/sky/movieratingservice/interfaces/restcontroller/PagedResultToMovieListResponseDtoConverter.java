package com.sky.movieratingservice.interfaces.restcontroller;

import com.sky.movieratingservice.domain.Movie;
import com.sky.movieratingservice.domain.PagedResult;
import com.sky.movieratingservice.openapi.interfaces.rest.dtos.MovieListResponseDto;
import com.sky.movieratingservice.openapi.interfaces.rest.dtos.PaginationInfoDto;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class PagedResultToMovieListResponseDtoConverter {

    private final MovieToMovieDtoConverter movieToMovieDtoConverter;

    public MovieListResponseDto convert(PagedResult<Movie> pagedResult) {
        // Map pagination info
        PaginationInfoDto paginationDto = new PaginationInfoDto()
                .pageNumber(pagedResult.getPageNumber())
                .pageSize(pagedResult.getPageSize())
                .totalRecords((int) pagedResult.getTotalElements());

        // Build and return the response DTO
        return MovieListResponseDto.builder()
                .paginationInfo(paginationDto)
                .movies(pagedResult.getContent().stream()
                        .map(movieToMovieDtoConverter::convert)
                        .collect(Collectors.toList()))
                .build();
    }
}