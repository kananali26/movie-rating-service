package com.sky.movieratingservice.interfaces.repositories.user;

import com.sky.movieratingservice.domain.Privilege;
import com.sky.movieratingservice.domain.Role;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class RoleDboToRoleConverter {

    private final PrivilegeDboToPrivilegeConverter privilegeDboToPrivilegeConverter;

    public Role convert(RoleDbo dbo) {
        List<Privilege> privileges = dbo.getPrivileges().stream()
            .map(privilegeDboToPrivilegeConverter::convert)
            .toList();

        return new Role(dbo.getId(), dbo.getName(), privileges);
    }
}
