package com.sky.movieratingservice.interfaces.repositories.user;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

interface UserJpaRepository extends JpaRepository<UserDbo, Long> {

    Optional<UserDbo> findByEmail(String email);

}