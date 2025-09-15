package com.sky.movieratingservice.domain;

import com.sky.movieratingservice.domain.exception.InvalidRequestException;
import java.util.ArrayList;
import java.util.List;

public record User(Long id, String email, String password, List<Role> roles) {

    public User(Long id, String email, String password, List<Role> roles) {
        if (email == null || email.trim().isEmpty()) {
            throw new InvalidRequestException("User email cannot be null or empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new InvalidRequestException("User password cannot be null or empty");
        }

        this.id = id;
        this.email = email;
        this.password = password;
        this.roles = roles != null ? roles : new ArrayList<>();
    }
}
