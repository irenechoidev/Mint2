package org.minttwo.controllers;

import org.minttwo.api.AccountDto;
import org.minttwo.api.GetAccountResponseDto;
import org.minttwo.api.ListAccountsResponseDto;
import org.minttwo.api.adapters.AccountAdapter;
import org.minttwo.dataclients.AccountClient;
import org.minttwo.models.Account;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountController implements AccountApi {
    private final AccountClient accountClient;
    private final AccountAdapter accountAdapter;

    public AccountController(AccountClient accountClient) {
        this.accountClient = accountClient;
        this.accountAdapter = new AccountAdapter();
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
    public void createAccount(Account account) {
        accountClient.createAccount(account);
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
}
