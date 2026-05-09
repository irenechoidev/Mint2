package org.minttwo.data.dataclients;

import lombok.NonNull;
import org.minttwo.data.DataClient;
import org.minttwo.data.Db;
import org.minttwo.data.exception.AccessDeniedException;
import org.minttwo.data.exception.NotFoundException;
import org.minttwo.data.models.UserModel;
import org.minttwo.data.validators.UserValidator;

import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.LocalDateTime;
import java.util.UUID;

public class UserClient extends DataClient<UserModel> {
    private final UserValidator validator;
    private final PasswordEncoder passwordEncoder;

    public UserClient(Db db, PasswordEncoder passwordEncoder) {
        super(db, UserModel.class);
        this.validator = new UserValidator();
        this.passwordEncoder = passwordEncoder;
    }

    public void createUser(@NonNull UserModel userModel) {
        validator.validate(userModel);

        String id = UUID.randomUUID().toString();
        String hashedPassword = passwordEncoder.encode(userModel.getPassword());
        userModel.setPassword(hashedPassword);
        userModel.setCreatedAt(LocalDateTime.now());
        userModel.setUpdatedAt(LocalDateTime.now());
        userModel.setId(id);

        this.insert(userModel);
    }

    public UserModel loadById(@NonNull String id) {
        UserModel userModel = this.getById(id);

        if (userModel == null) {
            String errMessage = String.format("User with id %s not found", id);
            throw new NotFoundException(errMessage, null);
        }

        return userModel;
    }

    public void loginUser(@NonNull UserModel requestUser) {
        UserModel userModel =
                this.getByUniqueField("username", requestUser.getUsername());

        if (userModel == null) {
            String errMessage = "User with username %s not found";
            throw new NotFoundException(String.format(errMessage, requestUser.getUsername()), null);
        }

        if (!passwordEncoder.matches(requestUser.getPassword(), userModel.getPassword())) {
            String errMessage = "Password for user with username %s is incorrect";
            throw new AccessDeniedException(String.format(errMessage, requestUser.getUsername()), null);
        }
    }
}
