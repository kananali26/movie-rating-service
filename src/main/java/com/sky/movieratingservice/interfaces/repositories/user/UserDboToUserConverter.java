package com.sky.movieratingservice.interfaces.repositories.user;

import com.sky.movieratingservice.domain.Role;
import com.sky.movieratingservice.domain.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class UserDboToUserConverter {

    private final RoleDboToRoleConverter roleDboToRoleConverter;

    public User convert(UserDbo dbo) {
        List<Role> roles = dbo.getRoles()
                .stream()
                .map(roleDboToRoleConverter::convert)
                .toList();

        return new User(dbo.getId().toString(), dbo.getEmail(), dbo.getPassword(), roles);
    }
}
