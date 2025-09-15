package com.sky.movieratingservice.usecases;

public interface PasswordHasher {
    String encode(char[] plain);
    boolean matches(char[] plain, String hash);
}
