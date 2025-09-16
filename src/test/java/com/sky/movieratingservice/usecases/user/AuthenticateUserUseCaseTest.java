package com.sky.movieratingservice.usecases.user;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.sky.movieratingservice.domain.User;
import com.sky.movieratingservice.domain.exception.UnauthorizedException;
import com.sky.movieratingservice.usecases.PasswordHasher;
import com.sky.movieratingservice.usecases.TokenProvider;
import com.sky.movieratingservice.usecases.repositories.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthenticateUserUseCaseTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private TokenProvider tokenProvider;
    @Mock
    private PasswordHasher passwordHasher;

    @Mock
    private User user;

    @InjectMocks
    private AuthenticateUserUseCase authenticateUserUseCase;

    @Captor
    private ArgumentCaptor<char[]> passwordCharsCaptor;

    @Test
    @DisplayName("authenticate() returns JWT when email exists and password matches")
    void authenticate_success() {
        String email = "john@doe.com";
        String rawPassword = "Secret#123";
        String hashedPassword = "$2a$10$abcdef...";
        String expectedToken = "jwt-token";

        when(user.password()).thenReturn(hashedPassword);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordHasher.matches(any(char[].class), eq(hashedPassword))).thenReturn(true);
        when(tokenProvider.issue(user)).thenReturn(expectedToken);

        String token = authenticateUserUseCase.authenticate(email, rawPassword);

        assertEquals(expectedToken, token);
        verify(userRepository).findByEmail(email);
        verify(passwordHasher).matches(passwordCharsCaptor.capture(), eq(hashedPassword));
        assertArrayEquals(rawPassword.toCharArray(), passwordCharsCaptor.getValue());
        verify(tokenProvider).issue(user);
        verifyNoMoreInteractions(tokenProvider);
    }

    @Test
    @DisplayName("authenticate() throws Unauthorized when user not found")
    void authenticate_userNotFound() {
        // arrange
        String email = "missing@user.com";
        String rawPassword = "whatever";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        UnauthorizedException ex = assertThrows(
                UnauthorizedException.class,
                () -> authenticateUserUseCase.authenticate(email, rawPassword)
        );
        assertEquals("Bad credentials", ex.getMessage());

        verify(userRepository).findByEmail(email);
        verify(passwordHasher, never()).matches(any(), anyString());
        verify(tokenProvider, never()).issue(any());
    }

    @Test
    @DisplayName("authenticate() throws Unauthorized when password does not match")
    void authenticate_badPassword() {
        // arrange
        String email = "john@doe.com";
        String rawPassword = "wrong";
        String hashedPassword = "$2a$10$abcdef...";

        when(user.password()).thenReturn(hashedPassword);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordHasher.matches(any(char[].class), eq(hashedPassword))).thenReturn(false);

        UnauthorizedException ex = assertThrows(
                UnauthorizedException.class,
                () -> authenticateUserUseCase.authenticate(email, rawPassword)
        );
        assertEquals("Bad credentials", ex.getMessage());

        verify(userRepository).findByEmail(email);
        verify(passwordHasher).matches(any(char[].class), eq(hashedPassword));
        verify(tokenProvider, never()).issue(any());
    }

}