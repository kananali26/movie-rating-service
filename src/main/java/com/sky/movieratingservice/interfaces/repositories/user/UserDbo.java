package com.sky.movieratingservice.interfaces.repositories.user;

import com.sky.movieratingservice.interfaces.repositories.BaseDbo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
@Entity
public class UserDbo extends BaseDbo {

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;
}
