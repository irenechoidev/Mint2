package org.minttwo.data.validators;

import lombok.NonNull;
import org.minttwo.api.exception.BadRequestException;
import org.minttwo.data.models.AccountTransaction;
import org.minttwo.data.models.AccountTransactionModel;

public class AccountTransactionValidator {
    public void validate(@NonNull AccountTransactionModel accountTransactionModel) {
        if (accountTransactionModel.getAccountId() == null || accountTransactionModel.getAccountId().isEmpty()) {
            throw new BadRequestException("AccountId is required and cannot be blank", null);
        }
        if (accountTransactionModel.getTitle() == null || accountTransactionModel.getTitle().isEmpty()) {
            throw new BadRequestException("Title is required and cannot be blank", null);
        }
        if (accountTransactionModel.getAmount() == null) {
            throw new BadRequestException("Amount is required", null);
        }
    }
}
