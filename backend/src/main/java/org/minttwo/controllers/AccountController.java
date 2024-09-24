package org.minttwo.controllers;

import org.minttwo.dataclients.AccountClient;
import org.minttwo.models.Account;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
