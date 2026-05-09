package org.minttwo.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.minttwo.data.models.AccountTransactionModel;
import org.minttwo.generated.api.AccountDto;
import org.minttwo.generated.api.AccountTransactionDto;
import org.minttwo.generated.api.CreateAccountDto;
import org.minttwo.generated.api.CreateAccountTransactionDto;
import org.minttwo.generated.api.GetAccountResponseDto;
import org.minttwo.data.dataclients.AccountClient;
import org.minttwo.data.dataclients.AccountTransactionClient;
import org.minttwo.data.exception.InvalidInputException;
import org.minttwo.data.exception.NotFoundException;
import org.minttwo.data.models.AccountModel;
import org.minttwo.generated.api.GetAccountTransactionResponseDto;
import org.minttwo.generated.api.ListAccountTransactionsResponseDto;
import org.minttwo.generated.api.ListAccountsResponseDto;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

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
    @Mock
    private AccountClient accountClient;

    @Mock
    private AccountTransactionClient accountTransactionClient;

    @InjectMocks
    private AccountController subject;

    @Captor
    private ArgumentCaptor<AccountModel> accountModelCaptor;

    @Captor
    private ArgumentCaptor<AccountTransactionModel> accountTransactionModelCaptor;

    @Test
    void testCreateAccount_200() {
        CreateAccountDto createAccountDto = CreateAccountDto.builder()
                .userId("this-is-user-id")
                .build();

        subject.createAccount(createAccountDto);
        verify(accountClient, times(1))
                .create(accountModelCaptor.capture());

        AccountModel accountModel = accountModelCaptor.getValue();
        assertThat(accountModel.getUserId()).isEqualTo("this-is-user-id");
    }

    @Test
    void testCreateAccount_400() {
        CreateAccountDto createAccountDto = CreateAccountDto.builder()
                .userId("this-is-user-id")
                .build();

        doThrow(new InvalidInputException("UserId is required and cannot be blank", null))
                .when(accountClient).create(any());

        assertThatThrownBy(() -> subject.createAccount(createAccountDto))
                .isInstanceOf(InvalidInputException.class)
                .hasMessage("UserId is required and cannot be blank");
    }

    @Test
    void testGetAccount_200() {
        when(accountClient.loadById(anyString())).thenReturn(AccountModel.builder()
                        .id("account-id-789")
                        .balance(233.20)
                        .userId("i-am-user-999")
                .build());

        String accountId = "account-id-789";
        ResponseEntity<GetAccountResponseDto> response = subject.getAccount(accountId);
        AccountDto accountDto = Optional.ofNullable(response.getBody())
                        .map(GetAccountResponseDto::getAccount)
                        .orElse(null);

        assertNotNull(accountDto);
        assertThat(accountDto.getId()).isEqualTo("account-id-789");
        assertThat(accountDto.getBalance()).isEqualTo(233.20);
        assertThat(accountDto.getUserId()).isEqualTo("i-am-user-999");
    }

    @Test
    void testGetAccount_404() {
        when(accountClient.loadById(anyString()))
                .thenThrow(new NotFoundException("Account with id account-id-789 not found", null));

        String accountId = "account-id-789";
        assertThatThrownBy(() -> subject.getAccount(accountId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(("Account with id account-id-789 not found"));
    }

    @Test
    void testListAccount_200(){
        when(accountClient.loadByUserId(anyString())).thenReturn(List.of(
                AccountModel.builder()
                        .id("account-id-123")
                        .balance(2192.0)
                        .userId("i-am-user-999")
                        .build(),
                AccountModel.builder()
                        .id("account-id-345")
                        .balance(300.1)
                        .userId("i-am-user-999")
                        .build()
        ));

        String userId = "i-am-user-999";
        ResponseEntity<ListAccountsResponseDto> response = subject.listAccounts(userId);
        List<AccountDto> accountDtoList = Optional.ofNullable(response.getBody())
                        .map(ListAccountsResponseDto::getAccounts)
                        .orElse(Collections.emptyList());
        AccountDto firstDto = accountDtoList.getFirst();
        AccountDto secondDto = accountDtoList.getLast();

        assertThat(accountDtoList).hasSize(2);
        assertThat(firstDto.getId()).isEqualTo("account-id-123");
        assertThat(firstDto.getBalance()).isEqualTo(2192.0);
        assertThat(firstDto.getUserId()).isEqualTo("i-am-user-999");
        assertThat(secondDto.getId()).isEqualTo("account-id-345");
        assertThat(secondDto.getBalance()).isEqualTo(300.1);
        assertThat(secondDto.getUserId()).isEqualTo("i-am-user-999");
    }

    @Test
    void testCreateAccountTransaction_200() {
        CreateAccountTransactionDto createAccountTransactionDto = CreateAccountTransactionDto.builder()
                .accountId("this-is-account-id")
                .amount(11.29)
                .build();
        when(accountClient.loadById(anyString())).thenReturn(AccountModel.builder()
                .id("account-id-789")
                .balance(233.20)
                .userId("i-am-user-999")
                .build());

        subject.createAccountTransaction(createAccountTransactionDto);
        verify(accountTransactionClient, times(1))
                .create(accountTransactionModelCaptor.capture());
        verify(accountClient, times(1))
                .update(accountModelCaptor.capture());

        AccountTransactionModel accountTransactionModel = accountTransactionModelCaptor.getValue();
        AccountModel accountModel = accountModelCaptor.getValue();
        assertThat(accountModel.getBalance()).isEqualTo(221.91);
        assertThat(accountTransactionModel.getAccountId()).isEqualTo("this-is-account-id");
        assertThat(accountTransactionModel.getAmount()).isEqualTo(11.29);
    }

    @Test
    void testCreateAccountTransaction_400() {
        CreateAccountTransactionDto createAccountTransactionDto = CreateAccountTransactionDto.builder()
                .accountId("this-is-account-id")
                .amount(11.29)
                .build();
        when(accountClient.loadById(anyString())).thenReturn(AccountModel.builder()
                .id("account-id-789")
                .balance(233.20)
                .userId("i-am-user-999")
                .build());

        doThrow(new InvalidInputException("AccountId is required and cannot be blank", null))
                .when(accountTransactionClient).create(any());

        assertThatThrownBy(() -> subject.createAccountTransaction(createAccountTransactionDto))
                .isInstanceOf(InvalidInputException.class)
                .hasMessage("AccountId is required and cannot be blank");
    }

    @Test
    void testCreateAccountTransaction_404() {
        CreateAccountTransactionDto createAccountTransactionDto = CreateAccountTransactionDto.builder()
                .accountId("this-is-account-id")
                .amount(11.29)
                .build();
        when(accountClient.loadById(anyString()))
                .thenThrow(new NotFoundException("Account with id account-id-789 not found", null));

        assertThatThrownBy(() -> subject.createAccountTransaction(createAccountTransactionDto))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(("Account with id account-id-789 not found"));
    }

    @Test
    void testGetAccountTransaction_200() {
        when(accountTransactionClient.loadById(anyString())).thenReturn(AccountTransactionModel.builder()
                .id("transaction-id-ggg")
                .accountId("account-id-324")
                .title("Amazon Prime")
                .amount(10.49)
                .build());

        String transactionId = "transaction-id-ggg";
        ResponseEntity<GetAccountTransactionResponseDto> response = subject.getAccountTransaction(transactionId);
        AccountTransactionDto accountTransactionDto = Optional.ofNullable(response.getBody())
                .map(GetAccountTransactionResponseDto::getAccountTransaction)
                .orElse(null);

        assertNotNull(accountTransactionDto);
        assertThat(accountTransactionDto.getId()).isEqualTo("transaction-id-ggg");
        assertThat(accountTransactionDto.getAccountId()).isEqualTo("account-id-324");
        assertThat(accountTransactionDto.getTitle()).isEqualTo("Amazon Prime");
        assertThat(accountTransactionDto.getAmount()).isEqualTo(10.49);
    }

    @Test
    void testGetAccountTransaction_404() {
        when(accountTransactionClient.loadById(anyString()))
                .thenThrow(new NotFoundException("AccountTransaction with id transaction-id-ggg not found", null));

        String transactionId = "transaction-id-ggg";
        assertThatThrownBy(() -> subject.getAccountTransaction(transactionId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("AccountTransaction with id transaction-id-ggg not found");
    }

    @Test
    void testListAccountTransactions_200() {
        when(accountTransactionClient.loadByAccountId(anyString())).thenReturn(List.of(
                AccountTransactionModel.builder()
                        .id("transaction-id-223")
                        .accountId("this-is-account-id")
                        .title("Uber Eats")
                        .amount(40.5)
                        .build(),
                AccountTransactionModel.builder()
                        .id("transaction-id-999")
                        .accountId("this-is-account-id")
                        .title("Piano")
                        .amount(5100.99)
                        .build()
        ));

        String accountId = "this-is-account-id";
        ResponseEntity<ListAccountTransactionsResponseDto> response = subject.listAccountTransactions(accountId);
        List<AccountTransactionDto> accountTransactionDtoList = Optional.ofNullable(response.getBody())
                .map(ListAccountTransactionsResponseDto::getAccountTransactions)
                .orElse(Collections.emptyList());
        AccountTransactionDto firstDto = accountTransactionDtoList.getFirst();
        AccountTransactionDto secondDto = accountTransactionDtoList.getLast();

        assertThat(accountTransactionDtoList).hasSize(2);
        assertThat(firstDto.getId()).isEqualTo("transaction-id-223");
        assertThat(firstDto.getAccountId()).isEqualTo("this-is-account-id");
        assertThat(firstDto.getTitle()).isEqualTo("Uber Eats");
        assertThat(firstDto.getAmount()).isEqualTo(40.5);
        assertThat(secondDto.getId()).isEqualTo("transaction-id-999");
        assertThat(secondDto.getAccountId()).isEqualTo("this-is-account-id");
        assertThat(secondDto.getTitle()).isEqualTo("Piano");
        assertThat(secondDto.getAmount()).isEqualTo(5100.99);
    }
}
