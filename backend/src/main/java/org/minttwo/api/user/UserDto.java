package org.minttwo.api.user;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserDto {
    private String id;
    private String email;
    private String username;
}
