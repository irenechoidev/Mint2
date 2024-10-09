package org.minttwo.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.minttwo.api.account.AccountDto;
import org.minttwo.api.account.GetAccountResponseDto;
import org.minttwo.api.account.ListAccountsResponseDto;
import org.minttwo.controllers.account.AccountController;
import org.minttwo.dataclients.AccountClient;
import org.minttwo.dataclients.AccountTransactionClient;
import org.minttwo.exception.BadRequestException;
import org.minttwo.exception.NotFoundException;
import org.minttwo.models.Account;
import org.minttwo.models.AccountTransaction;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountControllerTest {

    private static final String TEST_ACCOUNT_ID_PREFIX = "TEST_ACCOUNT_ID_";
    private static final String TEST_TRANSACTION_ID_PREFIX = "TEST_TRANSACTION_ID_";
    private static final String TEST_USER_ID = "TEST_USER_ID";

    @Mock
    private AccountClient accountClient;

    @Mock
    private AccountTransactionClient accountTransactionClient;

    @InjectMocks
    private AccountController subject;

    @Captor
    private ArgumentCaptor<Account> accountCaptor;

    @Captor
    private ArgumentCaptor<AccountTransaction> accountTransactionCaptor;

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
    void whenCallingCreateAccount_BadRequest() {
        int accountNumber = 1;
        Account expectedAccount = buildAccount(accountNumber);
        expectedAccount.setUserId("");

        String expectedErrMessage = "UserId is required and cannot be blank";

        doThrow(new BadRequestException(expectedErrMessage, null))
                .when(accountClient).createAccount(any());

        assertThatThrownBy(() -> subject.createAccount(expectedAccount))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(expectedErrMessage);
    }

    @Test
    void getAccountSuccess() {
        int accountNumber = 1;
        String accountId = TEST_ACCOUNT_ID_PREFIX + accountNumber;
        Account expectedAccount = buildAccount(accountNumber);
        when(accountClient.getAccount(anyString())).thenReturn(expectedAccount);

        ResponseEntity<GetAccountResponseDto> response = subject.getAccount(accountId);
        AccountDto testAccount = Optional.ofNullable(response.getBody())
                        .map(GetAccountResponseDto::getAccount)
                        .orElse(null);

        assertNotNull(testAccount);
        assertThat(testAccount.getId()).isEqualTo(expectedAccount.getId());
        assertThat(testAccount.getBalance()).isEqualTo(expectedAccount.getBalance());
        assertThat(testAccount.getUserId()).isEqualTo(expectedAccount.getUserId());
    }

    @Test
    void whenCallingGetAccount_ResourceNotFound() {
        int accountNumber = 1;
        String accountId = TEST_ACCOUNT_ID_PREFIX + accountNumber;
        String expectedErrMessage = String.format("Account with id %s not found", accountId);

        when(accountClient.getAccount(anyString()))
                .thenThrow(new NotFoundException(expectedErrMessage, null));

        assertThatThrownBy(() -> subject.getAccount(accountId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(expectedErrMessage);
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

        ResponseEntity<ListAccountsResponseDto> response = subject.listAccounts(TEST_USER_ID);
        List<AccountDto> testAccountsList = Optional.ofNullable(response.getBody())
                        .map(ListAccountsResponseDto::getAccounts)
                        .orElse(Collections.emptyList());


        AccountDto testAccount = testAccountsList.getFirst();
        Account expectedAccount = expectedAccountsList.getFirst();

        assertThat(testAccountsList.size()).isEqualTo(expectedAccountsList.size());
        assertThat(testAccount.getId()).isEqualTo(expectedAccount.getId());
        assertThat(testAccount.getUserId()).isEqualTo(expectedAccount.getUserId());
        assertThat(testAccount.getBalance()).isEqualTo(expectedAccount.getBalance());
    }

    @Test
    void createAccountTransactionSuccess() {
        int accountNumber = 1;
        AccountTransaction expectedAccountTransaction = buildAccountTransaction(accountNumber);

        subject.createAccountTransaction(expectedAccountTransaction);

        verify(accountTransactionClient, times(1))
                .createAccountTransaction(accountTransactionCaptor.capture());

        AccountTransaction testAccountTransaction = accountTransactionCaptor.getValue();
        assertThat(testAccountTransaction.getAccountId()).isEqualTo(expectedAccountTransaction.getAccountId());
        assertThat(testAccountTransaction.getAmount()).isEqualTo(expectedAccountTransaction.getAmount());
    }

    @Test
    void whenCallingCreateAccountTransaction_BadRequest() {
        int accountNumber = 1;
        AccountTransaction expectedAccountTransaction = buildAccountTransaction(accountNumber);
        expectedAccountTransaction.setAccountId("");

        String expectedErrMessage = "AccountId is required and cannot be blank";

        doThrow(new BadRequestException(expectedErrMessage, null))
                .when(accountTransactionClient).createAccountTransaction(any());

        assertThatThrownBy(() -> subject.createAccountTransaction(expectedAccountTransaction))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(expectedErrMessage);
    }

    private Account buildAccount(int index) {
        return Account.builder()
                .id(TEST_ACCOUNT_ID_PREFIX + index)
                .userId("Test-UserId")
                .balance(123.12)
                .build();
    }

    private AccountTransaction buildAccountTransaction(int index) {
        return AccountTransaction.builder()
                .id(TEST_TRANSACTION_ID_PREFIX + index)
                .accountId("Test-AccountId")
                .amount(12.22)
                .build();
    }
}
