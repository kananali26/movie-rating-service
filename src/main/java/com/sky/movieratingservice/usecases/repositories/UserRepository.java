package com.sky.movieratingservice.usecases.repositories;

import com.sky.movieratingservice.domain.User;
import java.util.Optional;

public interface UserRepository {

    Optional<User> findByEmail(String email);

    void register(String email, String password);
}
