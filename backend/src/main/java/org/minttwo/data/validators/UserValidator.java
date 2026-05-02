package org.minttwo.data.validators;

import lombok.NonNull;
import org.minttwo.data.exception.InvalidInputException;
import org.minttwo.data.models.UserModel;

public class UserValidator {
    public void validate(@NonNull UserModel userModel) {
        if (userModel.getEmail() == null || userModel.getEmail().isEmpty()) {
            throw new InvalidInputException("Email is required and cannot be blank", null);
        }
        if (userModel.getUsername() == null || userModel.getUsername().isEmpty()) {
            throw new InvalidInputException("Username is required and cannot be blank", null);
        }
        if (userModel.getPassword() == null || userModel.getPassword().isEmpty()) {
            throw new InvalidInputException("Password is required and cannot be blank", null);
        }
    }
}
