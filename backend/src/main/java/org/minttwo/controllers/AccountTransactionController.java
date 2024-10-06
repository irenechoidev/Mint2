package org.minttwo.controllers;

import org.minttwo.dataclients.AccountTransactionClient;
import org.minttwo.models.AccountTransaction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping ("/account_transaction")
public class AccountTransactionController implements  AccountTransactionApi {
    private final AccountTransactionClient accountTransactionClient;

    public AccountTransactionController(AccountTransactionClient accountTransactionClient) {
        this.accountTransactionClient = accountTransactionClient;
    }

    @Override
    public ResponseEntity<Void> createAccountTransaction (AccountTransaction accountTransaction) {
        accountTransactionClient.createAccountTransaction(accountTransaction);
        return ResponseEntity.ok().build();
    }
}
