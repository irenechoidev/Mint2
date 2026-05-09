package org.minttwo.data.dataclients;

import lombok.NonNull;
import org.minttwo.data.DataClient;
import org.minttwo.data.Db;
import org.minttwo.data.exception.NotFoundException;
import org.minttwo.data.models.AccountTransactionModel;
import org.minttwo.data.validators.AccountTransactionValidator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class AccountTransactionClient extends DataClient<AccountTransactionModel> {
    private static final String ACCOUNT_ID_FIELD_NAME = "accountId";
    private final AccountTransactionValidator validator;

    public AccountTransactionClient(Db db) {
        super(db, AccountTransactionModel.class);
        this.validator = new AccountTransactionValidator();
    }

    public void create(@NonNull AccountTransactionModel accountTransactionModel) {
        validator.validate(accountTransactionModel);

        String id = UUID.randomUUID().toString();
        accountTransactionModel.setCreatedAt(LocalDateTime.now());
        accountTransactionModel.setUpdatedAt(LocalDateTime.now());
        accountTransactionModel.setId(id);

        this.insert(accountTransactionModel);
    }

    public AccountTransactionModel loadById(@NonNull String id) {
        AccountTransactionModel accountTransaction = this.getById(id);

        if (accountTransaction == null) {
            String errMessage = String.format("AccountTransaction with id %s not found", id);
            throw new NotFoundException(errMessage, null);
        }

        return accountTransaction;
    }

    public List<AccountTransactionModel> loadByAccountId(@NonNull String accountId) {
        return this.getByField(ACCOUNT_ID_FIELD_NAME, accountId);
    }
}
