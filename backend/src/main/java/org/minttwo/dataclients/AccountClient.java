package org.minttwo.dataclients;

import lombok.NonNull;
import org.minttwo.models.Account;

import java.util.List;
import java.util.UUID;

public class AccountClient extends DataClient<Account> {
    private static final String USER_ID_FIELD_NAME = "userId";

    public AccountClient(Db db) {
        super(db);
    }

    public void createAccount(@NonNull Account account) {
        String id = UUID.randomUUID().toString();
        account.setId(id);

        this.insert(account);
    }

    public Account getAccount(@NonNull String id) {
        return this.getById(Account.class, id);
    }

    public List<Account> loadAccountsByUserId(@NonNull String userId) {
        return this.getByField(Account.class, USER_ID_FIELD_NAME, userId);
    }
}
