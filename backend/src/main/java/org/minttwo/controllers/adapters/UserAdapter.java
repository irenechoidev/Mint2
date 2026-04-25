package org.minttwo.controllers.adapters;

import lombok.NonNull;
import org.minttwo.data.models.UserModel;
import org.minttwo.generated.api.CreateUserDto;
import org.minttwo.generated.api.UserDto;


public class UserAdapter {
    @NonNull
    public UserDto toUserDto(@NonNull UserModel userModel){
        return UserDto.builder()
                .id(userModel.getId())
                .email(userModel.getEmail())
                .username(userModel.getUsername())
                .build();
    }

    @NonNull
    public UserModel toUserModel(@NonNull CreateUserDto createUserDto) {
        return UserModel.builder()
                .email(createUserDto.getEmail())
                .username(createUserDto.getUsername())
                .password(createUserDto.getPassword())
                .build();
    }
}
