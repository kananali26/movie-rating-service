package com.sky.movieratingservice.interfaces.repositories.movie;

import java.util.List;
import java.util.Objects;
import java.math.BigDecimal;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;

interface MovieJpaRepository extends JpaRepository<MovieDbo, Long>, JpaSpecificationExecutor<MovieDbo> {

    @Modifying
    @Transactional
    @Query("UPDATE MovieDbo m SET m.ratingCount = :count, m.averageRating = :average WHERE m.id = :id")
    void updateRatingCountAndAverageById(Long id, Integer count, BigDecimal average);

    @Query("SELECT m FROM MovieDbo m ORDER BY m.averageRating DESC")
    List<MovieDbo> findTopRatedMovies(Pageable pageable);


    interface Specifications {

        @NonNull
        static Specification<MovieDbo> filterByName(@Nullable String name) {
            return (root, query, builder) -> {
                if (Objects.isNull(name) || name.trim().isEmpty()) {
                    return builder.conjunction();
                }

                return builder.like(
                    builder.lower(root.get("name")), 
                    "%" + name.toLowerCase() + "%"
                );
            };
        }
    }
}
