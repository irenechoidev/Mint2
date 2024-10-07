package org.minttwo.api.user;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GetUserResponseDto {
    private UserDto user;
}
