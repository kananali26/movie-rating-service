package com.sky.movieratingservice.usecases.user;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.sky.movieratingservice.domain.User;
import com.sky.movieratingservice.domain.exception.NotFoundException;
import com.sky.movieratingservice.usecases.repositories.UserRepository;
import java.util.Optional;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetUserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private User user;

    @InjectMocks
    private GetUserUseCase getUserUseCase;

    @Test
    @DisplayName("getUser(email) returns the user when found")
    void testGetUserWhenFound() {
        String email = Instancio.create(String.class);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        User result = getUserUseCase.getUser(email);

        assertSame(user, result);
        verify(userRepository, times(1)).findByEmail(email);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("getUser(email) throws NotFoundException with email in message when not found")
    void getUserThrowExceptionWhenNotFound() {
        String email = Instancio.create(String.class);
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> getUserUseCase.getUser(email));

        assertEquals(String.format("User with email:%s not found", email), ex.getMessage());
        verify(userRepository, times(1)).findByEmail(email);
        verifyNoMoreInteractions(userRepository);
    }

}