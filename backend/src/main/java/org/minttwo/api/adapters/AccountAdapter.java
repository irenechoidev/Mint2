package org.minttwo.api.adapters;

import lombok.NonNull;
import org.minttwo.api.AccountDto;
import org.minttwo.models.Account;

public class AccountAdapter {
    public AccountDto adapt(@NonNull Account account) {
        return AccountDto.builder()
                .id(account.getId())
                .userId(account.getUserId())
                .balance(account.getBalance())
                .build();
    }
}
