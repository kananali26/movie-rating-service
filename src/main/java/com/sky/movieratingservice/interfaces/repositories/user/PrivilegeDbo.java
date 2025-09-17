package com.sky.movieratingservice.interfaces.repositories.user;

import com.sky.movieratingservice.interfaces.repositories.BaseDbo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "privileges")
@Entity
public class PrivilegeDbo extends BaseDbo {

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "privileges")
    private Set<RoleDbo> roles;
}
