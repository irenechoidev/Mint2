package org.minttwo.controllers.account;

import org.minttwo.api.account.AccountDto;
import org.minttwo.api.account.AccountTransactionDto;
import org.minttwo.api.account.GetAccountResponseDto;
import org.minttwo.api.account.GetAccountTransactionResponseDto;
import org.minttwo.api.account.ListAccountTransactionsResponseDto;
import org.minttwo.api.account.ListAccountsResponseDto;
import org.minttwo.api.account.adapters.AccountAdapter;
import org.minttwo.api.account.adapters.AccountTransactionAdapter;
import org.minttwo.dataclients.AccountClient;
import org.minttwo.dataclients.AccountTransactionClient;
import org.minttwo.models.Account;
import org.minttwo.models.AccountTransaction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountController implements AccountApi {
    private final AccountClient accountClient;
    private final AccountTransactionClient accountTransactionClient;
    private final AccountAdapter accountAdapter;
    private final AccountTransactionAdapter accountTransactionAdapter;

    public AccountController(
            AccountClient accountClient,
            AccountTransactionClient accountTransactionClient
    ) {
        this.accountClient = accountClient;
        this.accountTransactionClient = accountTransactionClient;
        this.accountAdapter = new AccountAdapter();
        this.accountTransactionAdapter = new AccountTransactionAdapter();
    }

    @Override
    public ResponseEntity<GetAccountResponseDto> getAccount(String id) {
        Account account = accountClient.getAccount(id);
        AccountDto accountDto = accountAdapter.adapt(account);

        GetAccountResponseDto getAccountDto = GetAccountResponseDto.builder()
                .account(accountDto)
                .build();

        return ResponseEntity.ok(getAccountDto);
    }

    @Override
    public ResponseEntity<Void> createAccount(Account account) {
        accountClient.createAccount(account);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<ListAccountsResponseDto> listAccounts(String userId) {
        List<Account> accounts = accountClient.loadAccountsByUserId(userId);
        List<AccountDto> accountDtos = accounts.stream()
                .map(accountAdapter::adapt)
                .toList();

        ListAccountsResponseDto listAccountsDto = ListAccountsResponseDto.builder()
                .accounts(accountDtos)
                .build();

        return ResponseEntity.ok(listAccountsDto);
    }

    @Override
    public ResponseEntity<Void> createAccountTransaction(AccountTransaction accountTransaction) {
        accountTransactionClient.createAccountTransaction(accountTransaction);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<GetAccountTransactionResponseDto> getAccountTransaction(String id) {
        AccountTransaction accountTransaction = accountTransactionClient.getAccountTransaction(id);
        AccountTransactionDto accountTransactionDto = accountTransactionAdapter.adapt(accountTransaction);

        GetAccountTransactionResponseDto getAccountTransactionDto = GetAccountTransactionResponseDto.builder()
                .accountTransaction(accountTransactionDto)
                .build();

        return ResponseEntity.ok(getAccountTransactionDto);
    }

    @Override
    public ResponseEntity<ListAccountTransactionsResponseDto> listAccountTransactions(String accountId) {

        List<AccountTransaction> accountTransactions = accountTransactionClient.loadAccountTransactionsByAccountId(accountId);
        List<AccountTransactionDto> accountTransactionDto = accountTransactions.stream()
                .map(accountTransactionAdapter::adapt)
                .toList();

        ListAccountTransactionsResponseDto listAccountTransactionsResponseDto = ListAccountTransactionsResponseDto
                .builder()
                .accountTransactionDtoList(accountTransactionDto)
                .build();

        return ResponseEntity.ok(listAccountTransactionsResponseDto);
    }
}
