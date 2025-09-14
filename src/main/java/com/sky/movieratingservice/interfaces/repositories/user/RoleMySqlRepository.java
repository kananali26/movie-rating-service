package com.sky.movieratingservice.interfaces.repositories.user;

import com.sky.movieratingservice.domain.Role;
import com.sky.movieratingservice.usecases.repositories.RoleRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class RoleMySqlRepository implements RoleRepository {

    private final RoleJpaRepository roleJpaRepository;
    private final RoleDboToRoleConverter roleDboToRoleConverter;

    @Override
    public Optional<Role> getByName(String name) {
        return roleJpaRepository
                .findByName(name)
                .map(roleDboToRoleConverter::convert);
    }
}
