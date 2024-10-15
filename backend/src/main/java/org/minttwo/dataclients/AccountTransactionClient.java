package org.minttwo.dataclients;

import lombok.NonNull;
import org.minttwo.exception.NotFoundException;
import org.minttwo.models.Account;
import org.minttwo.models.AccountTransaction;
import org.minttwo.validators.AccountTransactionValidator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class AccountTransactionClient extends DataClient<AccountTransaction> {
    private static final String ACCOUNT_ID_FIELD_NAME = "accountId";
    private final AccountTransactionValidator validator;

    public AccountTransactionClient(Db db) {
        super(db);
        this.validator = new AccountTransactionValidator();
    }

    public void createAccountTransaction(@NonNull AccountTransaction accountTransaction) {
        validator.validate(accountTransaction);

        String id = UUID.randomUUID().toString();
        accountTransaction.setCreatedAt(LocalDateTime.now());
        accountTransaction.setUpdatedAt(LocalDateTime.now());
        accountTransaction.setId(id);

        this.insert(accountTransaction);
    }

    public AccountTransaction getAccountTransaction(@NonNull String id) {
        AccountTransaction accountTransaction = this.getById(AccountTransaction.class, id);

        if (accountTransaction == null) {
            String errMessage = String.format("AccountTransaction with id %s not found", id);
            throw new NotFoundException(errMessage, null);
        }

        return accountTransaction;
    }

    public List<AccountTransaction> loadAccountTransactionsByAccountId(@NonNull String accountId) {
        return this.getByField(AccountTransaction.class, ACCOUNT_ID_FIELD_NAME, accountId);
    }
}
