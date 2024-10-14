package org.minttwo.api.account;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AccountTransactionDto {
    private String id;
    private String accountId;
    private String title;
    private double amount;
}
