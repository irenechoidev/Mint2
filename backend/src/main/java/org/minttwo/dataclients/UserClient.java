package org.minttwo.dataclients;

import lombok.NonNull;
import org.minttwo.exception.NotFoundException;
import org.minttwo.models.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserClient extends DataClient<User> {
    private final PasswordEncoder passwordEncoder;

    public UserClient(Db db) {
        super(db);
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public void createUser(@NonNull User user) {
        String id = UUID.randomUUID().toString();
        String hashedPassword = passwordEncoder.encode(user.getPassword());

        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setPassword(hashedPassword);
        user.setId(id);

        this.insert(user);
    }

    public User getUserById(@NonNull String id) {
        User user = this.getById(User.class, id);

        if (user == null) {
            String errMessage = String.format("User with id %s not found", id);
            throw new NotFoundException(errMessage, null);
        }
        return user;
    }
}
