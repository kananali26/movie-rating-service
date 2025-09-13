package com.sky.movieratingservice.domain;

import lombok.Getter;

@Getter
public class Movie {
    private final long id;
    private String name;

    public Movie(long id, String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Movie title cannot be null or empty");
        }

        this.id = id;
        this.name = name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Movie title cannot be null or empty");
        }
        this.name = name;
    }


}
