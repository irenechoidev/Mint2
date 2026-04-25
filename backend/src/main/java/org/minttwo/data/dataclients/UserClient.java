package org.minttwo.data.dataclients;

import lombok.NonNull;
import org.minttwo.api.exception.NotFoundException;
import org.minttwo.data.models.UserModel;
import org.minttwo.data.validators.UserValidator;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserClient extends DataClient<UserModel> {
    private static final String USERNAME_FIELD_NAME = "username";

    private final UserValidator validator;

    public UserClient(Db db) {
        super(db);
        this.validator = new UserValidator();
    }

    public void createUser(@NonNull UserModel userModel) {
        validator.validate(userModel);

        String id = UUID.randomUUID().toString();

        userModel.setCreatedAt(LocalDateTime.now());
        userModel.setUpdatedAt(LocalDateTime.now());
        userModel.setId(id);

        this.insert(userModel);
    }

    public UserModel loadById(@NonNull String id) {
        UserModel userModel = this.getById(UserModel.class, id);

        if (userModel == null) {
            String errMessage = String.format("User with id %s not found", id);
            throw new NotFoundException(errMessage, null);
        }
        return userModel;
    }

    public UserModel getByUsername(@NonNull String username) {
        return this.getByUniqueField(UserModel.class, USERNAME_FIELD_NAME, username);
    }
}
