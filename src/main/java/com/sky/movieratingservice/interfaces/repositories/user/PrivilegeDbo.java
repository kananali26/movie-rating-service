package com.sky.movieratingservice.interfaces.repositories.user;

import com.sky.movieratingservice.interfaces.repositories.BaseDbo;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "privilege")
@Entity
public class PrivilegeDbo extends BaseDbo {
}
