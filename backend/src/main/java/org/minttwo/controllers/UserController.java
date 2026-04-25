package org.minttwo.controllers;

import org.minttwo.controllers.adapters.UserAdapter;
import org.minttwo.data.dataclients.UserClient;
import org.minttwo.api.exception.AccessDeniedException;
import org.minttwo.api.exception.NotFoundException;
import org.minttwo.data.models.UserModel;
import org.minttwo.generated.api.CreateUserDto;
import org.minttwo.generated.api.GetUserResponseDto;
import org.minttwo.generated.api.LoginUserDto;
import org.minttwo.generated.api.UserDto;
import org.minttwo.generated.api.UsersApi;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController implements UsersApi {
    private final UserClient userClient;
    private final UserAdapter userAdapter;
    private final PasswordEncoder passwordEncoder;

    public UserController(
        UserClient userClient,
        PasswordEncoder passwordEncoder
    ) {
        this.userClient = userClient;
        this.passwordEncoder = passwordEncoder;
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
    public ResponseEntity<Void> loginUser(LoginUserDto requestDto) {
        UserModel userModel = userClient.getByUsername(requestDto.getUsername());
        if (userModel == null) {
            String errMessage = "User with username %s not found";
            throw new NotFoundException(String.format(errMessage, requestDto.getUsername()), null);
        }

        if (!passwordEncoder.matches(requestDto.getPassword(), userModel.getPassword())) {
            String errMessage = "Password for user with username %s is incorrect";
            throw new AccessDeniedException(String.format(errMessage, requestDto.getUsername()), null);
        }

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> createUser(CreateUserDto createUserDto) {
        String hashedPassword = passwordEncoder.encode(createUserDto.getPassword());
        createUserDto.setPassword(hashedPassword);
        UserModel userModel = userAdapter.toUserModel(createUserDto);

        userClient.createUser(userModel);
        return ResponseEntity.ok().build();
    }
}
