package com.sky.movieratingservice.interfaces.restcontroller.authentication;

import com.sky.movieratingservice.openapi.interfaces.rest.AuthenticationApi;
import com.sky.movieratingservice.openapi.interfaces.rest.dtos.LoginRequestDto;
import com.sky.movieratingservice.openapi.interfaces.rest.dtos.TokenResponseDto;
import com.sky.movieratingservice.usecases.user.AuthenticateUserUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthenticationRestController implements AuthenticationApi {

    private final AuthenticateUserUseCase authenticateUserUseCase;

    @Override
    public ResponseEntity<TokenResponseDto> login(LoginRequestDto loginRequestDto) {
        String token = authenticateUserUseCase.authenticate(loginRequestDto.getEmail(), loginRequestDto.getPassword());
        TokenResponseDto tokenResponseDto = new TokenResponseDto();
        tokenResponseDto.accessToken(token);
        return ResponseEntity.ok(tokenResponseDto);
    }
}
