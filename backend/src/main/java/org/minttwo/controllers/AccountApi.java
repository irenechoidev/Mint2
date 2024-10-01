package org.minttwo.controllers;

import org.minttwo.api.GetAccountResponseDto;
import org.minttwo.api.ListAccountsResponseDto;
import org.minttwo.models.Account;
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
}
