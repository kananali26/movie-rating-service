package com.sky.movieratingservice.interfaces.repositories.movie;

import com.sky.movieratingservice.interfaces.repositories.BaseDbo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "movie")
@Entity
public class MovieDbo extends BaseDbo {

    @Column(name = "name", nullable = false)
    private String name;
}
