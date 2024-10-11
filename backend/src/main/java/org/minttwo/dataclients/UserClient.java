package org.minttwo.dataclients;

import lombok.NonNull;
import org.minttwo.exception.NotFoundException;
import org.minttwo.models.User;
import org.minttwo.validators.UserValidator;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserClient extends DataClient<User> {
    private static final String USERNAME_FIELD_NAME = "username";

    private final UserValidator validator;

    public UserClient(Db db) {
        super(db);
        this.validator = new UserValidator();
    }

    public void createUser(@NonNull User user) {
        validator.validate(user);

        String id = UUID.randomUUID().toString();

        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
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

    public User getByUsername(@NonNull String username) {
        return this.getByUniqueField(User.class, USERNAME_FIELD_NAME, username);
    }
}
