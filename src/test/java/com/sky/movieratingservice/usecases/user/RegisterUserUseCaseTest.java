package com.sky.movieratingservice.usecases.user;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.sky.movieratingservice.domain.Role;
import com.sky.movieratingservice.domain.User;
import com.sky.movieratingservice.domain.exception.InvalidRequestException;
import com.sky.movieratingservice.domain.exception.NotFoundException;
import com.sky.movieratingservice.usecases.PasswordHasher;
import com.sky.movieratingservice.usecases.repositories.RoleRepository;
import com.sky.movieratingservice.usecases.repositories.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RegisterUserUseCaseTest {

    @Mock
    private PasswordHasher passwordHasher;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private Role role;
    @Mock
    private User user;

    @InjectMocks
    private RegisterUserUseCase registerUserUseCase;

    @Test
    @DisplayName("register() should create user with encoded password when email is free and ROLE_USER exists")
    void register_success() {
        String email = "john.doe@example.com";
        String rawPassword = "P@ssw0rd!";
        String encoded = "$2a$10$abcdef..."; // whatever your encoder would return

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(roleRepository.getByName("ROLE_USER")).thenReturn(Optional.of(role));
        when(passwordHasher.encode(any(char[].class))).thenReturn(encoded);

        registerUserUseCase.register(email, rawPassword);

        ArgumentCaptor<char[]> pwCaptor = ArgumentCaptor.forClass(char[].class);
        verify(passwordHasher).encode(pwCaptor.capture());
        assertEquals(rawPassword, new String(pwCaptor.getValue()));

        verify(userRepository).findByEmail(email);
        verify(roleRepository).getByName("ROLE_USER");
        verify(userRepository).register(email, encoded, role);

        verifyNoMoreInteractions(userRepository, roleRepository, passwordHasher);
    }

    @Test
    @DisplayName("register() should throw InvalidRequestException when email already exists")
    void register_emailAlreadyExists() {
        String email = "exists@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        InvalidRequestException ex = assertThrows(
                InvalidRequestException.class,
                () -> registerUserUseCase.register(email, "irrelevant")
        );
        assertEquals(String.format("Email '%s' already exists", email), ex.getMessage());

        verify(userRepository).findByEmail(email);
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(roleRepository, passwordHasher);
    }

    @Test
    @DisplayName("register() should throw NotFoundException when ROLE_USER is missing")
    void register_roleMissing() {
        String email = "new@example.com";
        String password = "secret";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(roleRepository.getByName("ROLE_USER")).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> registerUserUseCase.register(email, password)
        );
        assertEquals(String.format("Role with name '%s' doesn't exist", "ROLE_USER"), ex.getMessage());

        verify(userRepository).findByEmail(email);
        verify(roleRepository).getByName("ROLE_USER");
        verify(userRepository, never()).register(anyString(), anyString(), any());
        verifyNoInteractions(passwordHasher);
        verifyNoMoreInteractions(userRepository, roleRepository);
    }

}