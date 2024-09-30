package org.minttwo.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.minttwo.api.AccountDto;
import org.minttwo.api.ListAccountsResponseDto;
import org.minttwo.dataclients.AccountClient;
import org.minttwo.models.Account;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountControllerTest {

    private static final String TEST_ACCOUNT_ID_PREFIX = "TEST_ACCOUNT_ID_";
    private static final String TEST_USER_ID = "TEST_USER_ID";

    @Mock
    private AccountClient accountClient;

    @InjectMocks
    private AccountController subject;

    @Captor
    private ArgumentCaptor<Account> accountCaptor;

    @Test
    void createAccountSuccess() {
        int accountNumber = 1;
        Account expectedAccount = buildAccount(accountNumber);

        subject.createAccount(expectedAccount);

        verify(accountClient, times(1)).createAccount(accountCaptor.capture());
        Account testAccount = accountCaptor.getValue();

        assertThat(testAccount.getUserId()).isEqualTo(expectedAccount.getUserId());
        assertThat(testAccount.getBalance()).isEqualTo(expectedAccount.getBalance());
    }

    @Test
    void getAccountSuccess() {
        int accountNumber = 1;
        String accountId = TEST_ACCOUNT_ID_PREFIX + accountNumber;
        Account expectedAccount = buildAccount(accountNumber);
        when(accountClient.getAccount(anyString())).thenReturn(expectedAccount);

        Account testAccount = subject.getAccount(accountId);

        assertThat(testAccount.getId()).isEqualTo(expectedAccount.getId());
        assertThat(testAccount.getBalance()).isEqualTo(expectedAccount.getBalance());
        assertThat(testAccount.getUserId()).isEqualTo(expectedAccount.getUserId());
    }

    @Test
    void listAccountsSuccess(){
        int numAccounts = 10;
        List<Account> expectedAccountsList = new ArrayList<>();
        for (int i = 0; i < numAccounts; i++) {
            Account account = buildAccount(i);
            expectedAccountsList.add(account);
        }

       when(accountClient.loadAccountsByUserId(anyString())).thenReturn(expectedAccountsList);

        ResponseEntity<ListAccountsResponseDto> listAccountsDto = subject.listAccounts(TEST_USER_ID);
        List<AccountDto> testAccountsList = Optional.ofNullable(listAccountsDto.getBody())
                        .map(ListAccountsResponseDto::getAccounts)
                        .orElse(Collections.emptyList());


        AccountDto testAccount = testAccountsList.getFirst();
        Account expectedAccount = expectedAccountsList.getFirst();

        assertThat(testAccountsList.size()).isEqualTo(expectedAccountsList.size());
        assertThat(testAccount.getId()).isEqualTo(expectedAccount.getId());
        assertThat(testAccount.getUserId()).isEqualTo(expectedAccount.getUserId());
        assertThat(testAccount.getBalance()).isEqualTo(expectedAccount.getBalance());
    }

    private Account buildAccount(int index) {
        return Account.builder()
                .id(TEST_ACCOUNT_ID_PREFIX + index)
                .userId("Test-UserId")
                .balance(123.12)
                .build();
    }
}
