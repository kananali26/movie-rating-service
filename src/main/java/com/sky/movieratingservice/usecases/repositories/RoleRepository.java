package com.sky.movieratingservice.usecases.repositories;

import com.sky.movieratingservice.domain.Role;
import java.util.Optional;

public interface RoleRepository {

    Optional<Role> getByName(String name);
}
