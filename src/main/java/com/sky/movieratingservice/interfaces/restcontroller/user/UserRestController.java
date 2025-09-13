package com.sky.movieratingservice.interfaces.restcontroller.user;

import com.sky.movieratingservice.openapi.interfaces.rest.UserApi;
import com.sky.movieratingservice.openapi.interfaces.rest.dtos.RegisterUserRequestDto;
import com.sky.movieratingservice.usecases.user.RegisterUserUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserRestController implements UserApi {

    private final RegisterUserUseCase registerUserUseCase;

    @Override
    public ResponseEntity<Void> registerUser(RegisterUserRequestDto registerUserRequestDto) {
        registerUserUseCase.register(registerUserRequestDto.getEmail(), registerUserRequestDto.getPassword());

        return ResponseEntity.status(201).build();
    }
}
