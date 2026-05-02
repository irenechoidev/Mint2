package org.minttwo.data.validators;

import lombok.NonNull;
import org.minttwo.data.exception.InvalidInputException;
import org.minttwo.data.models.AccountTransactionModel;

public class AccountTransactionValidator {
    public void validate(@NonNull AccountTransactionModel accountTransactionModel) {
        if (accountTransactionModel.getAccountId() == null || accountTransactionModel.getAccountId().isEmpty()) {
            throw new InvalidInputException("AccountId is required and cannot be blank", null);
        }
        if (accountTransactionModel.getTitle() == null || accountTransactionModel.getTitle().isEmpty()) {
            throw new InvalidInputException("Title is required and cannot be blank", null);
        }
        if (accountTransactionModel.getAmount() == null) {
            throw new InvalidInputException("Amount is required", null);
        }
    }
}
