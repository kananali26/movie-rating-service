package com.sky.movieratingservice.interfaces.repositories.user;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

interface UserJpaRepository extends JpaRepository<UserDbo, Long> {

    @Query("SELECT u FROM UserDbo u LEFT JOIN FETCH u.roles r LEFT JOIN FETCH r.privileges WHERE u.email = :email")
    Optional<UserDbo> findByEmail(String email);

}