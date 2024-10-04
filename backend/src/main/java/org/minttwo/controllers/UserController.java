package org.minttwo.controllers;

import org.minttwo.api.adapters.UserAdapter;
import org.minttwo.api.GetUserResponseDto;
import org.minttwo.api.UserDto;
import org.minttwo.dataclients.UserClient;
import org.minttwo.models.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController implements UserApi {
    private final UserClient userClient;
    private final UserAdapter userAdapter;

    public UserController(UserClient userClient) {
        this.userClient = userClient;
        this.userAdapter = new UserAdapter();
    }

    @Override
    public ResponseEntity<Void> createUser(User user) {
        userClient.createUser(user);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<GetUserResponseDto> getUser(String id) {
        User user = userClient.getUserById(id);
        UserDto userDto = userAdapter.adapt(user);
        GetUserResponseDto getUserDto = GetUserResponseDto.builder()
                    .user(userDto)
                    .build();
        return ResponseEntity.ok(getUserDto);
    }
}
