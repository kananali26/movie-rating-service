package com.sky.movieratingservice.interfaces.repositories.user;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

interface UserJpaRepository extends JpaRepository<UserDbo, Long>, JpaSpecificationExecutor<UserDbo> {

    Optional<UserDbo> findByEmail(String email);

}