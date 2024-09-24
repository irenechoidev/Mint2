package org.minttwo.controllers;

import org.minttwo.models.Account;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface AccountApi {
    @GetMapping("/{id}")
    public Account getAccount(@PathVariable String id);

    @PostMapping("/")
    public void createAccount(@RequestBody Account account);

    @GetMapping("/list/{userId}")
    public List<Account> listAccounts(@PathVariable String userId);
}
