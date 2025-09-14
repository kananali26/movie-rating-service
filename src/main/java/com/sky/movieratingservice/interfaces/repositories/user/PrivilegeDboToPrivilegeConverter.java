package com.sky.movieratingservice.interfaces.repositories.user;

import com.sky.movieratingservice.domain.Privilege;
import org.springframework.stereotype.Component;

@Component
class PrivilegeDboToPrivilegeConverter {

    public Privilege convert(PrivilegeDbo privilegeDbo) {
        return new Privilege(privilegeDbo.getId(), privilegeDbo.getName());
    }
}
