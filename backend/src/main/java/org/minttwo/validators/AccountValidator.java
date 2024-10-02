package org.minttwo.validators;

import lombok.NonNull;
import org.minttwo.exception.BadRequestException;
import org.minttwo.models.Account;

public class AccountValidator {
    public void validate(@NonNull Account account) {
        if (account.getUserId() == null || account.getUserId().isEmpty()) {
            throw new BadRequestException("UserId is required and cannot be blank", null);
        }

        if (account.getBalance() == null) {
            throw new BadRequestException("Balance is required", null);
        }
    }
}
