package org.minttwo.api.account.adapters;

import lombok.NonNull;
import org.minttwo.api.account.AccountTransactionDto;
import org.minttwo.models.AccountTransaction;

public class AccountTransactionAdapter {
    @NonNull
    public AccountTransactionDto adapt(@NonNull AccountTransaction accountTransaction) {
        return AccountTransactionDto.builder()
                .id(accountTransaction.getId())
                .accountId(accountTransaction.getAccountId())
                .title(accountTransaction.getTitle())
                .amount(accountTransaction.getAmount())
                .build();
    }
}
