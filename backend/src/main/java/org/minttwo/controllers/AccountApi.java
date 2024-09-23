package org.minttwo.controllers;

import org.minttwo.models.Account;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface AccountApi {
    @GetMapping("/")
    public Account getAccount();

    @PostMapping("/")
    public void createAccount(@RequestBody Account account);
}
