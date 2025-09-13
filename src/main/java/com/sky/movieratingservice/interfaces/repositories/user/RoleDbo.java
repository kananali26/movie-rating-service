package com.sky.movieratingservice.interfaces.repositories.user;

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
@Table(name = "role")
@Entity
public class RoleDbo extends BaseDbo {

    @Column(nullable = false, unique = true)
    private String name;
}
