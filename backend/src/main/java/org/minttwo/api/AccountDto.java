package org.minttwo.api;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AccountDto {
    private String id;
    private String userId;
    private double balance;
}
