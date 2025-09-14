package com.sky.movieratingservice.domain;

import java.util.List;

public record Role(Long id, String name, List<Privilege> privileges) {
}
