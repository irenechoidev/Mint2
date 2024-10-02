package org.minttwo.validators;

import lombok.NonNull;
import org.minttwo.exception.BadRequestException;
import org.minttwo.models.User;

public class UserValidator {
    public void validate(@NonNull User user) {
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new BadRequestException("Email is required and cannot be blank", null);
        }
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            throw new BadRequestException("Username is required and cannot be blank", null);
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new BadRequestException("Password is required and cannot be blank", null);
        }
    }
}
