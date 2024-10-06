package org.minttwo.validators;

import lombok.NonNull;
import org.minttwo.exception.BadRequestException;
import org.minttwo.models.AccountTransaction;

public class AccountTransactionValidator {
    public void validate(@NonNull AccountTransaction accountTransaction) {
        if (accountTransaction.getAccountId() == null || accountTransaction.getAccountId().isEmpty()) {
            throw new BadRequestException("AccountId is required and cannot be blank", null);
        }
        if (accountTransaction.getAmount() == null) {
            throw new BadRequestException("Amount is required", null);
        }
    }
}
