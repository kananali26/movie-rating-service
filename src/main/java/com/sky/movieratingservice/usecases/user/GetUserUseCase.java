package com.sky.movieratingservice.usecases.user;

import com.sky.movieratingservice.domain.User;
import com.sky.movieratingservice.domain.exception.NotFoundException;
import com.sky.movieratingservice.usecases.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetUserUseCase {

    private final UserRepository userRepository;

    public User getUser(String email) {
        return userRepository
                .findByEmail(email)
                .orElseThrow(() -> new NotFoundException(String.format("User with email:%s not found", email)));
    }
}
