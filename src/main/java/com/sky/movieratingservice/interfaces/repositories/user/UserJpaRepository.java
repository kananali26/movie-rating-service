package com.sky.movieratingservice.interfaces.repositories.user;

import java.util.Objects;
import java.util.Optional;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

interface UserJpaRepository extends JpaRepository<UserDbo, Long>, JpaSpecificationExecutor<UserDbo> {

    Optional<UserDbo> findByEmail(String email);

    interface Specifications {

        @NonNull
        static Specification<UserDbo> filterByEmail(@Nullable String email) {
            return (root, query, builder) -> {
                if (Objects.isNull(email) || email.trim().isEmpty()) {
                    return builder.conjunction(); // Return always true predicate if no email filter
                }

                return builder.like(
                    builder.lower(root.get("email")), 
                    "%" + email.toLowerCase() + "%"
                );
            };
        }
    }
}