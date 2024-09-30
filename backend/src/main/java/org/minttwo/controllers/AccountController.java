package org.minttwo.controllers;

import org.minttwo.api.ListAccountsResponseDto;
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

    public AccountController(AccountClient accountClient) {
        this.accountClient = accountClient;
    }

    @Override
    public Account getAccount(String id) {
        return accountClient.getAccount(id);
    }

    @Override
    public void createAccount(Account account) {
        accountClient.createAccount(account);
    }

    @Override
    public ResponseEntity<ListAccountsResponseDto> listAccounts(String userId) {
        List<Account> accounts = accountClient.loadAccountsByUserId(userId);

        ListAccountsResponseDto listAccountsDto = ListAccountsResponseDto.builder()
                .accounts(accounts)
                .build();

        return ResponseEntity.ok(listAccountsDto);
    }
}
