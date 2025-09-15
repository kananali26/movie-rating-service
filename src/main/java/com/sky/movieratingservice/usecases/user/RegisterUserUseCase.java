package com.sky.movieratingservice.usecases.user;

import com.sky.movieratingservice.domain.Role;
import com.sky.movieratingservice.domain.exception.InvalidRequestException;
import com.sky.movieratingservice.domain.exception.NotFoundException;
import com.sky.movieratingservice.usecases.PasswordHasher;
import com.sky.movieratingservice.usecases.repositories.RoleRepository;
import com.sky.movieratingservice.usecases.repositories.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegisterUserUseCase {

    private static final String ROLE_USER = "ROLE_USER";

    private final PasswordHasher passwordHasher;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Transactional
    public void register(String email, String password) {

        if(userRepository.findByEmail(email).isPresent()){
            throw new InvalidRequestException(String.format("Email '%s' already exists", email));
        }

        Optional<Role> optionalRole = roleRepository.getByName(ROLE_USER);

        if(optionalRole.isEmpty()){
            throw new NotFoundException(String.format("Role with name '%s' doesn't exist", ROLE_USER));
        }

        userRepository.register(email, passwordHasher.encode(password.toCharArray()), optionalRole.get());
    }
}
