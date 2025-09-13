package com.sky.movieratingservice.interfaces.repositories.user;

import com.sky.movieratingservice.domain.User;
import org.springframework.stereotype.Component;

@Component
class UserDboToUserConverter {

    public User convert(UserDbo dbo) {
        return new User(dbo.getId().toString(), dbo.getEmail(), dbo.getPassword());
    }
}
