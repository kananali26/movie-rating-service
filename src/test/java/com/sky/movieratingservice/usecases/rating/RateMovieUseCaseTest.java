package com.sky.movieratingservice.usecases.rating;

import static org.junit.jupiter.api.Assertions.*;

import com.sky.movieratingservice.usecases.movie.GetMovieUseCase;
import com.sky.movieratingservice.usecases.movie.UpsertMovieUseCase;
import com.sky.movieratingservice.usecases.repositories.RatingRepository;
import com.sky.movieratingservice.usecases.user.GetUserUseCase;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RateMovieUseCaseTest {

    @Mock
    private GetUserUseCase getUserUseCase;
    @Mock
    private GetMovieUseCase getMovieUseCase;
    @Mock
    private UpsertMovieUseCase upsertMovieUseCase;
    @Mock
    private RatingRepository ratingRepository;

    @InjectMocks
    private RateMovieUseCase rateMovieUseCase;

}