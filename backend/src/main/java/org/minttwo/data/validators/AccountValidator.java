package org.minttwo.data.validators;

import lombok.NonNull;
import org.minttwo.api.exception.BadRequestException;
import org.minttwo.data.models.AccountModel;

public class AccountValidator {
    public void validate(@NonNull AccountModel account) {
        if (account.getUserId() == null || account.getUserId().isEmpty()) {
            throw new BadRequestException("UserId is required and cannot be blank", null);
        }

        if (account.getBalance() == null) {
            throw new BadRequestException("Balance is required", null);
        }
    }
}
