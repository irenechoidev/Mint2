package org.minttwo.dataclients;

import lombok.NonNull;
import org.minttwo.models.Account;

public class AccountClient extends DataClient<Account> {
    public AccountClient(Db db) {
        super(db);
    }

    public void createAccount(@NonNull Account account) {
        this.insert(account);
    }
}
