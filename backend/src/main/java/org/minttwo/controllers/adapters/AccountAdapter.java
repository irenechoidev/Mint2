package org.minttwo.controllers.adapters;

import lombok.NonNull;
import org.minttwo.data.models.AccountModel;
import org.minttwo.data.models.AccountTransactionModel;
import org.minttwo.generated.api.AccountDto;
import org.minttwo.generated.api.AccountTransactionDto;
import org.minttwo.generated.api.CreateAccountDto;
import org.minttwo.generated.api.CreateAccountTransactionDto;

public class AccountAdapter {
    @NonNull
    public AccountDto toAccountDto(@NonNull AccountModel accountModel) {
        return AccountDto.builder()
                .id(accountModel.getId())
                .userId(accountModel.getUserId())
                .name(accountModel.getName())
                .balance(accountModel.getBalance())
                .build();
    }

    @NonNull
    public AccountModel toCreateAccountModel(@NonNull CreateAccountDto createAccountDto) {
        return AccountModel.builder()
                .userId(createAccountDto.getUserId())
                .name(createAccountDto.getName())
                .build();
    }

    @NonNull
    public AccountTransactionDto toAccountTransactionDto(
            @NonNull AccountTransactionModel accountTransactionModel) {
        return AccountTransactionDto.builder()
                .id(accountTransactionModel.getId())
                .accountId(accountTransactionModel.getAccountId())
                .title(accountTransactionModel.getTitle())
                .amount(accountTransactionModel.getAmount())
                .build();
    }

    @NonNull
    public AccountTransactionModel toCreateAccountTransactionModel(
            @NonNull CreateAccountTransactionDto createAccountTransactionDto) {
        return AccountTransactionModel.builder()
                .accountId(createAccountTransactionDto.getAccountId())
                .amount(createAccountTransactionDto.getAmount())
                .title(createAccountTransactionDto.getTitle())
                .build();
    }
}
