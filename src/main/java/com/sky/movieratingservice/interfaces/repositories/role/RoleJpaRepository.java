package com.sky.movieratingservice.interfaces.repositories.role;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleJpaRepository extends JpaRepository<RoleDbo, Long> {

    Optional<RoleDbo> findByName(String name);
}
