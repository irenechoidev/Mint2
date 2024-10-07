package org.minttwo.api.user.adapters;

import lombok.NonNull;
import org.minttwo.api.user.UserDto;
import org.minttwo.models.User;


public class UserAdapter {
    @NonNull
    public UserDto adapt(@NonNull User user){
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .build();
    }
}
