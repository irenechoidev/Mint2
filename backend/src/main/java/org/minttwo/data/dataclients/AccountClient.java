package org.minttwo.data.dataclients;

import lombok.NonNull;
import org.minttwo.data.DataClient;
import org.minttwo.data.Db;
import org.minttwo.data.exception.NotFoundException;
import org.minttwo.data.models.AccountModel;
import org.minttwo.data.validators.AccountValidator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class AccountClient extends DataClient<AccountModel> {
    private final AccountValidator validator;

    public AccountClient(Db db) {
        super(db, AccountModel.class);
        this.validator = new AccountValidator();
    }

    public void create(@NonNull AccountModel accountModel) {
        validator.validate(accountModel);

        String id = UUID.randomUUID().toString();
        accountModel.setCreatedAt(LocalDateTime.now());
        accountModel.setUpdatedAt(LocalDateTime.now());
        accountModel.setId(id);

        this.insert(accountModel);
    }

    public AccountModel loadById(@NonNull String id) {
        AccountModel accountModel = this.getById(id);

        if (accountModel == null) {
            String errMessage = String.format("Account with id %s not found", id);
            throw new NotFoundException(errMessage, null);
        }

        return accountModel;
    }

    public List<AccountModel> loadByUserId(@NonNull String userId) {
        return this.getByField("userId", userId);
    }
}
