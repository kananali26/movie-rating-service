package com.sky.movieratingservice.usecases;

import com.sky.movieratingservice.domain.User;
import java.time.Duration;

public interface TokenProvider {

    String issue(User user, Duration ttl);
}
