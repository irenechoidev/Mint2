package org.minttwo.api;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class ListAccountsResponseDto {
    private List<AccountDto> accounts;
}
