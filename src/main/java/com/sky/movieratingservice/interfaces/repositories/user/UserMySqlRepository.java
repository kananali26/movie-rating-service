package com.sky.movieratingservice.interfaces.repositories.user;

import com.sky.movieratingservice.domain.User;
import com.sky.movieratingservice.usecases.repositories.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class UserMySqlRepository implements UserRepository {

    private final UserJpaRepository userJpaRepository;
    private final UserDboToUserConverter userDboToUserConverter;

    @Override
    public Optional<User> findByEmail(String email) {
        Optional<UserDbo> optionalUserDbo = userJpaRepository.findByEmail(email);
        return optionalUserDbo.map(userDboToUserConverter::convert);
    }

    @Override
    public void register(String email, String password) {
        UserDbo userDbo = new UserDbo();
        userDbo.setEmail(email);
        userDbo.setPassword(password);

        userJpaRepository.save(userDbo);
    }
}
