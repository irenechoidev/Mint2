package org.minttwo.api.user;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LoginUserRequestDto {
    private String username;
    private String password;
}
