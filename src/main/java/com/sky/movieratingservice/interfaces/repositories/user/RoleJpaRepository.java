package com.sky.movieratingservice.interfaces.repositories.user;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

interface RoleJpaRepository extends JpaRepository<RoleDbo, Long> {

    Optional<RoleDbo> findByName(String name);
}
