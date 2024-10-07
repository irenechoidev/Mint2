package org.minttwo.api.account.adapters;

import lombok.NonNull;
import org.minttwo.api.account.AccountDto;
import org.minttwo.models.Account;

public class AccountAdapter {
    @NonNull
    public AccountDto adapt(@NonNull Account account) {
        return AccountDto.builder()
                .id(account.getId())
                .userId(account.getUserId())
                .balance(account.getBalance())
                .build();
    }
}
