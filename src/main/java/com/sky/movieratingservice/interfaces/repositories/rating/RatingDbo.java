package com.sky.movieratingservice.interfaces.repositories.rating;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sky.movieratingservice.interfaces.repositories.BaseDbo;
import com.sky.movieratingservice.interfaces.repositories.movie.MovieDbo;
import com.sky.movieratingservice.interfaces.repositories.user.UserDbo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ratings")
@Entity
public class RatingDbo extends BaseDbo {

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UserDbo user;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "movie_id", referencedColumnName = "id", nullable = false)
    private MovieDbo movie;

    @Column(name = "rating", nullable = false)
    private Integer rating;
}
