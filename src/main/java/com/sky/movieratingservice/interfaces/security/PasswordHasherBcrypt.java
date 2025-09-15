package com.sky.movieratingservice.interfaces.security;

import com.sky.movieratingservice.usecases.PasswordHasher;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasswordHasherBcrypt implements PasswordHasher {

    private final PasswordEncoder passwordEncoder;

    @Override
    public String encode(char[] plain) {
        return passwordEncoder.encode(new String(plain));
    }

    @Override
    public boolean matches(char[] plain, String hash) {
        return passwordEncoder.matches(new String(plain), hash);
    }
}
