package com.sky.movieratingservice.interfaces.repositories.movie;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sky.movieratingservice.interfaces.repositories.BaseDbo;
import com.sky.movieratingservice.interfaces.repositories.rating.RatingDbo;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.math.BigDecimal;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "movies")
@Entity
public class MovieDbo extends BaseDbo {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "rating_count")
    private Integer ratingCount;

    @Column(name = "average_rating")
    private BigDecimal averageRating;

    @Version
    @Column(name = "version")
    private Long version;

    @JsonIgnore
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RatingDbo> ratings;
}
