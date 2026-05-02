package org.minttwo.controllers.adapters;

import lombok.NonNull;
import org.minttwo.data.models.UserModel;
import org.minttwo.generated.api.CreateUserDto;
import org.minttwo.generated.api.LoginUserDto;
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
    public UserModel toCreateUserModel(@NonNull CreateUserDto createUserDto) {
        return UserModel.builder()
                .email(createUserDto.getEmail())
                .username(createUserDto.getUsername())
                .password(createUserDto.getPassword())
                .build();
    }

    @NonNull
    public UserModel toLoginUserModel(@NonNull LoginUserDto loginUserDto) {
        return UserModel.builder()
                .username(loginUserDto.getUsername())
                .password(loginUserDto.getPassword())
                .build();
    }
}
