package org.minttwo.api;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GetUserResponseDto {
    private UserDto user;
}
