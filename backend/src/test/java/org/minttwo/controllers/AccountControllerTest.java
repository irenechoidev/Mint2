package org.minttwo.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.minttwo.dataclients.AccountClient;
import org.minttwo.models.Account;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AccountControllerTest {

    @Mock
    private AccountClient accountClient;

    @InjectMocks
    private AccountController subject;

    @Captor
    private ArgumentCaptor<Account> accountCaptor;

    @Test
    void createAccountSuccess() {
        Account expectedAccount = buildAccount();

        subject.createAccount(expectedAccount);

        verify(accountClient, times(1)).createAccount(accountCaptor.capture());
        Account testAccount = accountCaptor.getValue();

        assertThat(testAccount.getUserId()).isEqualTo(expectedAccount.getUserId());
        assertThat(testAccount.getBalance()).isEqualTo(expectedAccount.getBalance());
    }

    private Account buildAccount() {
        return Account.builder()
                .userId("Test-UserId")
                .balance(123.12)
                .build();
    }
}
