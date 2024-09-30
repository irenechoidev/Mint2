package org.minttwo.api;

import lombok.Builder;
import lombok.Data;
import org.minttwo.models.Account;

import java.util.List;

@Builder
@Data
public class ListAccountsResponseDto {
    private List<Account> accounts;
}
