package org.minttwo.api;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserDto {
    private String id;
    private String email;
    private String username;
}
