package com.sky.movieratingservice.usecases.user;

import com.sky.movieratingservice.domain.exception.InvalidRequestException;
import com.sky.movieratingservice.usecases.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterUserUseCase {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public void register(String email, String password) {
        if(userRepository.findByEmail(email).isPresent()){
            throw new InvalidRequestException("Email already exists");
        }

        userRepository.register(email, passwordEncoder.encode(password));
    }
}
