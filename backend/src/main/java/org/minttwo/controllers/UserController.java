package org.minttwo.controllers;

import org.minttwo.controllers.adapters.UserAdapter;
import org.minttwo.data.dataclients.UserClient;
import org.minttwo.data.models.UserModel;
import org.minttwo.generated.api.CreateUserDto;
import org.minttwo.generated.api.GetUserResponseDto;
import org.minttwo.generated.api.LoginUserDto;
import org.minttwo.generated.api.UserDto;
import org.minttwo.generated.api.UsersApi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController implements UsersApi {
    private final UserClient userClient;
    private final UserAdapter userAdapter;

    public UserController(UserClient userClient) {
        this.userClient = userClient;
        this.userAdapter = new UserAdapter();
    }

    @Override
    public ResponseEntity<GetUserResponseDto> getUser(String id) {
        UserModel userModel = userClient.loadById(id);
        UserDto userDto = userAdapter.toUserDto(userModel);

        GetUserResponseDto getUserDto = GetUserResponseDto.builder()
                .user(userDto)
                .build();
        return ResponseEntity.ok(getUserDto);
    }

    @Override
    public ResponseEntity<Void> loginUser(LoginUserDto loginUserDto) {
        UserModel userModel = userAdapter.toLoginUserModel(loginUserDto);
        userClient.loginUser(userModel);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> createUser(CreateUserDto createUserDto) {
        UserModel userModel = userAdapter.toCreateUserModel(createUserDto);
        userClient.createUser(userModel);
        return ResponseEntity.ok().build();
    }
}
