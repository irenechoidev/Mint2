package org.minttwo.dataclients;

import lombok.NonNull;
import org.minttwo.models.Account;

import java.util.UUID;

public class AccountClient extends DataClient<Account> {
    public AccountClient(Db db) {
        super(db);
    }

    public void createAccount(@NonNull Account account) {
        String id = UUID.randomUUID().toString();
        account.setId(id);

        this.insert(account);
    }

    public Account getAccount(String id) {
        return this.getById(Account.class, id);
    }
}
