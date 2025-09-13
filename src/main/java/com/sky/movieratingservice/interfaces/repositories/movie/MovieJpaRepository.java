package com.sky.movieratingservice.interfaces.repositories.movie;

import java.util.Objects;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

interface MovieJpaRepository extends JpaRepository<MovieDbo, Long>, JpaSpecificationExecutor<MovieDbo> {

    interface Specifications {

        @NonNull
        static Specification<MovieDbo> filterByName(@Nullable String name) {
            return (root, query, builder) -> {
                if (Objects.isNull(name) || name.trim().isEmpty()) {
                    return builder.conjunction(); // Return always true predicate if no name filter
                }

                return builder.like(
                    builder.lower(root.get("name")), 
                    "%" + name.toLowerCase() + "%"
                );
            };
        }
    }
}
