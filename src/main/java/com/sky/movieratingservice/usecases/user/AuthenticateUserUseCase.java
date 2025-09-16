package com.sky.movieratingservice.usecases.user;

import com.sky.movieratingservice.domain.User;
import com.sky.movieratingservice.domain.exception.UnauthorizedException;
import com.sky.movieratingservice.usecases.PasswordHasher;
import com.sky.movieratingservice.usecases.TokenProvider;
import com.sky.movieratingservice.usecases.repositories.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticateUserUseCase {

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final PasswordHasher passwordHasher;

    public String authenticate(String email, String password) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty() || !passwordHasher.matches(password.toCharArray(), optionalUser.get().password())) {
            throw new UnauthorizedException("Bad credentials");
        }

        return tokenProvider.issue(optionalUser.get());
    }
}
