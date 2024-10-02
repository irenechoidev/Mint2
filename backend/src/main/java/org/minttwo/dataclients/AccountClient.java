package org.minttwo.dataclients;

import lombok.NonNull;
import org.minttwo.exception.NotFoundException;
import org.minttwo.models.Account;
import org.minttwo.validators.AccountValidator;

import java.util.List;
import java.util.UUID;

public class AccountClient extends DataClient<Account> {
    private static final String USER_ID_FIELD_NAME = "userId";

    private final AccountValidator validator;

    public AccountClient(Db db) {
        super(db);
        this.validator = new AccountValidator();
    }

    public void createAccount(@NonNull Account account) {
        validator.validate(account);

        String id = UUID.randomUUID().toString();
        account.setId(id);

        this.insert(account);
    }

    public Account getAccount(@NonNull String id) {
        Account account = this.getById(Account.class, id);

        if (account == null) {
            String errMessage = String.format("Account with id %s not found", id);
            throw new NotFoundException(errMessage, null);
        }

        return account;
    }

    public List<Account> loadAccountsByUserId(@NonNull String userId) {
        return this.getByField(Account.class, USER_ID_FIELD_NAME, userId);
    }
}
