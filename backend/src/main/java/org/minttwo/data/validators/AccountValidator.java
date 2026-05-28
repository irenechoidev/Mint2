package org.minttwo.data.validators;

import lombok.NonNull;
import org.minttwo.data.exception.InvalidInputException;
import org.minttwo.data.models.AccountModel;

public class AccountValidator {
    public void validate(@NonNull AccountModel account) {
        if (account.getUserId() == null || account.getUserId().isEmpty()) {
            throw new InvalidInputException("UserId is required and cannot be blank", null);
        }
        if (account.getName() == null || account.getName().isEmpty()) {
            throw new InvalidInputException("Name is required and cannot be blank", null);
        }
    }
}
