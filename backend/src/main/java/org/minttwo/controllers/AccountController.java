package org.minttwo.controllers;

import org.minttwo.controllers.adapters.AccountAdapter;
import org.minttwo.data.dataclients.AccountClient;
import org.minttwo.data.dataclients.AccountTransactionClient;
import org.minttwo.data.models.AccountModel;
import org.minttwo.data.models.AccountTransactionModel;
import org.minttwo.generated.api.AccountDto;
import org.minttwo.generated.api.AccountTransactionDto;
import org.minttwo.generated.api.AccountsApi;
import org.minttwo.generated.api.CreateAccountDto;
import org.minttwo.generated.api.CreateAccountTransactionDto;
import org.minttwo.generated.api.GetAccountResponseDto;
import org.minttwo.generated.api.GetAccountTransactionResponseDto;
import org.minttwo.generated.api.ListAccountTransactionsResponseDto;
import org.minttwo.generated.api.ListAccountsResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AccountController implements AccountsApi {
    private final AccountClient accountClient;
    private final AccountTransactionClient accountTransactionClient;
    private final AccountAdapter accountAdapter;

    public AccountController(
            AccountClient accountClient,
            AccountTransactionClient accountTransactionClient
    ) {
        this.accountClient = accountClient;
        this.accountTransactionClient = accountTransactionClient;
        this.accountAdapter = new AccountAdapter();
    }

    @Override
    public ResponseEntity<GetAccountResponseDto> getAccount(String id) {
        AccountModel accountModel = accountClient.loadById(id);
        AccountDto accountDto = accountAdapter.toAccountDto(accountModel);

        GetAccountResponseDto getAccountDto = GetAccountResponseDto.builder()
                .account(accountDto)
                .build();

        return ResponseEntity.ok(getAccountDto);
    }

    @Override
    public ResponseEntity<ListAccountsResponseDto> listAccounts(String userId) {
        List<AccountModel> accounts = accountClient.loadByUserId(userId);
        List<AccountDto> accountDtos = accounts.stream()
                .map(accountAdapter::toAccountDto)
                .toList();

        ListAccountsResponseDto listAccountsDto = ListAccountsResponseDto.builder()
                .accounts(accountDtos)
                .build();

        return ResponseEntity.ok(listAccountsDto);
    }

    @Override
    public ResponseEntity<Void> createAccount(CreateAccountDto createAccountDto) {
        AccountModel accountModel = accountAdapter.toCreateAccountModel(createAccountDto);
        accountClient.create(accountModel);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<GetAccountTransactionResponseDto> getAccountTransaction(String id) {
        AccountTransactionModel accountTransactionModel = accountTransactionClient.loadById(id);
        AccountTransactionDto accountTransactionDto =
                accountAdapter.toAccountTransactionDto(accountTransactionModel);

        GetAccountTransactionResponseDto getAccountTransactionDto = GetAccountTransactionResponseDto.builder()
                .accountTransaction(accountTransactionDto)
                .build();

        return ResponseEntity.ok(getAccountTransactionDto);
    }

    @Override
    public ResponseEntity<ListAccountTransactionsResponseDto> listAccountTransactions(String accountId) {
        List<AccountTransactionModel> accountTransactions = accountTransactionClient.loadByAccountId(accountId);
        List<AccountTransactionDto> accountTransactionDtos = accountTransactions.stream()
                .map(accountAdapter::toAccountTransactionDto)
                .toList();

        ListAccountTransactionsResponseDto listAccountTransactionsResponseDto = ListAccountTransactionsResponseDto
                .builder()
                .accountTransactions(accountTransactionDtos)
                .build();

        return ResponseEntity.ok(listAccountTransactionsResponseDto);
    }

    @Override
    public ResponseEntity<Void> createAccountTransaction(
            CreateAccountTransactionDto createAccountTransactionDto) {
        AccountModel accountModel = accountClient.loadById(
                createAccountTransactionDto.getAccountId()
        );
        accountClient.update(
                accountModel.toBuilder()
                        .balance(accountModel.getBalance() - createAccountTransactionDto.getAmount())
                        .build()
        );

        AccountTransactionModel accountTransactionModel =
                accountAdapter.toCreateAccountTransactionModel(createAccountTransactionDto);
        accountTransactionClient.create(accountTransactionModel);
        return ResponseEntity.ok().build();
    }
}
