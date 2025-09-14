package com.sky.movieratingservice.interfaces.repositories.role;

import com.sky.movieratingservice.domain.Role;
import com.sky.movieratingservice.usecases.repositories.RoleRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class RoleMySqlRepository implements RoleRepository {

    private final RoleJpaRepository roleJpaRepository;

    @Override
    public Optional<Role> getByName(String name) {
        return roleJpaRepository.findByName(name)
                .map(roleDbo -> {
                    Role role = new Role();
                    role.setId(roleDbo.getId());
                    role.setName(roleDbo.getName());
                    return role;
                });
    }
}
