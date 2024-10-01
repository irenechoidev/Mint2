package org.minttwo.api;

import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class GetAccountResponseDto {
    private AccountDto account;
}
