package org.minttwo.controllers.account;

import org.minttwo.api.account.GetAccountResponseDto;
import org.minttwo.api.account.GetAccountTransactionResponseDto;
import org.minttwo.api.account.ListAccountsResponseDto;
import org.minttwo.models.Account;
import org.minttwo.models.AccountTransaction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface AccountApi {
    @GetMapping("/{id}")
    public ResponseEntity<GetAccountResponseDto> getAccount(@PathVariable String id);

    @PostMapping("/")
    public ResponseEntity<Void> createAccount(@RequestBody Account account);

    @GetMapping("/list/{userId}")
    public ResponseEntity<ListAccountsResponseDto> listAccounts(@PathVariable String userId);

    @PostMapping("/account_transaction")
    public ResponseEntity<Void> createAccountTransaction(
            @RequestBody AccountTransaction accountTransaction
    );

    @GetMapping("/account_transaction/{id}")
    public ResponseEntity<GetAccountTransactionResponseDto> getAccountTransaction(
            @PathVariable String id
    );
}
