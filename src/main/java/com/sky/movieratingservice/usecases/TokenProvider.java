package com.sky.movieratingservice.usecases;

import com.sky.movieratingservice.domain.User;

public interface TokenProvider {

    String issue(User user);

    boolean validate(String token, String subject);
}
