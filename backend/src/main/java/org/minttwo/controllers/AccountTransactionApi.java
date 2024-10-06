package org.minttwo.controllers;

import org.minttwo.models.AccountTransaction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface AccountTransactionApi {
    @PostMapping("/")
    public ResponseEntity<Void> createAccountTransaction(
            @RequestBody AccountTransaction accountTransaction
    );
}
